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

package com.family.diary.common.models.redis;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

/**
 * Redis属性值
 *
 * @author Richard Zhang
 * @since 2025-07-23
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {
    /**
     * Redis服务器的版本
     */
    private String version;

    /**
     * Redis服务器的主机名
     */
    private String host;

    /**
     * Redis服务器的端口号
     */
    private int port;

    /**
     * Redis服务器的用户名
     */
    private String username;

    /**
     * Redis服务器的密码
     */
    private String password;

    /**
     * Redis客户端配置
     */
    private LettuceProperties lettuce;

    /**
     * 判断 Redis 版本是否小于 6.0.0
     *
     * @return true if version < 6.0.0
     */
    public boolean isVersionLessThan6() {
        if (version == null || version.isEmpty()) {
            return false; // 无法判断，保守返回 false
        }

        // 可选：过滤掉非数字和点的字符（如 "Redis 7.0.5" -> "7.0.5"）
        String cleanVersion = version.replaceAll("[^0-9.]", "");

        String[] parts = cleanVersion.split(Pattern.quote("."));
        if (parts.length == 0) {
            return false;
        }

        try {
            int major = Integer.parseInt(parts[0]);
            if (major < 6) {
                return true;
            } else if (major == 6) {
                int minor = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
                if (minor < 0) {
                    return true;
                }
            }
            return false;
        } catch (NumberFormatException e) {
            log.error("无法解析 Redis 版本号: {}", cleanVersion, e);
            return false; // 解析失败，保守返回 false
        }
    }
}
