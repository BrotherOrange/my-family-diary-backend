/*
 * Copyright (c) 2024 Richard Zhang (richard.jih.zhang@gmail.com).
 * Website: https://zhang-jihao.com
 * All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.family.diary.api.filters.common;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.family.diary.common.constants.response.ResponseMessageConstants;
import com.family.diary.common.enums.errors.ResponseErrorCode;
import com.family.diary.common.utils.common.CommonResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 用于检测请求头是否包含了TraceId的Filter
 *
 * @author Richard Zhang
 * @since 2025-11-19
 */
@Slf4j
public class TraceIdFilter extends OncePerRequestFilter {

    /**
     * 不需要 TraceId 的路径（Swagger、Actuator 等）
     */
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/swagger-ui",
            "/v3/api-docs",
            "/actuator"
    );

    @Value("${log-trace.trace-header}")
    private String traceHeader;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        // getServletPath() 在某些代理场景下可能返回空字符串
        // 使用 getRequestURI() 并去掉 context path 更可靠
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();

        // 移除 context path 获取实际的 servlet path
        String path = requestUri;
        if (contextPath != null && !contextPath.isEmpty() && requestUri.startsWith(contextPath)) {
            path = requestUri.substring(contextPath.length());
        }

        boolean shouldSkip = EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
        // 临时使用 info 级别方便调试，之后改回 debug
        log.info("TraceIdFilter check - requestUri: {}, contextPath: {}, path: {}, shouldSkip: {}",
                requestUri, contextPath, path, shouldSkip);

        return shouldSkip;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final var traceId = request.getHeader(traceHeader);

        // 检查 traceId 是否存在且非空白
        if (traceId == null || StringUtils.isBlank(traceId)) {
            log.warn("Missing or empty trace header '{}' in request from {}:{}",
                    traceHeader,
                    request.getRemoteAddr(),
                    request.getRequestURI());

            CommonResponse.writeErrorResponse(
                    response,
                    ResponseErrorCode.BAD_REQUEST,
                    ResponseMessageConstants.MISSING_TRACE_ID,
                    Map.of("requiredHeader", traceHeader)
            );
            return;
        }

        MDC.put("traceId", traceId.trim());

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
