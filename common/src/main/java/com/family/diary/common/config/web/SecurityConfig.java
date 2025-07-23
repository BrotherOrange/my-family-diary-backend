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

package com.family.diary.common.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security配置类
 *
 * @author Richard Zhang
 * @since 2025-07-11
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * 定义一个SecurityFilterChain Bean，用于配置HTTP安全性
     *
     * @param http HttpSecurity对象
     * @return SecurityFilterChain对象
     * @throws Exception 如果配置过程中发生错误
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers("/**")
                                        .permitAll() // 放行注册接口
                                        .anyRequest()
                                        .authenticated() // 其他接口需要认证
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
