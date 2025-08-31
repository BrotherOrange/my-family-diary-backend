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

package com.family.diary.api.utils.web.jwt;

import com.family.diary.api.service.user.UserService;
import com.family.diary.common.utils.web.jwt.JwtUtil;
import com.family.diary.domain.entity.user.UserEntity;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    @Value("${jwt.token-header}")
    private String tokenHeader;

    @Value("${jwt.token-prefix}")
    private String tokenPrefix;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(tokenHeader);

        String openId = null;
        String jwt = null;

        // 检查Authorization Header是否存在且以"Bearer "开头
        if (authorizationHeader != null && authorizationHeader.startsWith(tokenPrefix)) {
            jwt = authorizationHeader.substring(tokenPrefix.trim().length() + 1); // 去掉"Bearer "前缀
            try {
                openId = jwtUtil.extractOpenId(jwt);
            } catch (ExpiredJwtException e) {
                logger.warn("JWT Token已过期", e);
            } catch (MalformedJwtException e) {
                logger.warn("JWT Token无效", e);
            }
        }

        // 如果提取到了用户名，并且当前没有认证
        if (openId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserEntity userDetails = userService.findByOpenId(openId);

            // 验证Token
            if (userDetails != null && jwtUtil.validateToken(jwt, openId)) {
                // 创建认证对象
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, new ArrayList<>());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 设置认证信息到SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
