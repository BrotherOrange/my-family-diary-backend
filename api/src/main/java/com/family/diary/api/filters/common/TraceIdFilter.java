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
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 用于检测请求头是否包含了TraceId的Filter
 *
 * @author Richard Zhang
 * @since 2025-11-19
 */
@Slf4j
public class TraceIdFilter extends OncePerRequestFilter {
    @Value("${log-trace.trace-header}")
    private String traceHeader;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                              FilterChain filterChain) throws ServletException, IOException {
        final var traceId = request.getHeader(traceHeader);

        // 检查 traceId 是否存在且非空白
        if (traceId == null || StringUtils.isBlank(traceId)) {
            log.warn("Missing or empty trace header '{}' in request from {}:{}",
                    traceHeader,
                    request.getRemoteAddr(),
                    request.getRequestURI());

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("""
                {"error": "Missing or empty trace ID", "requiredHeader": "%s"}
                """.formatted(traceHeader));
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
