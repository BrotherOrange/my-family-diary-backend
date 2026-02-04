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

package com.family.diary.common.enums.jwt;

import lombok.Getter;

/**
 * JWT Token类型枚举
 * 封装不同类型Token的配置信息
 *
 * @author Richard Zhang
 * @since 2025-08-21
 */
@Getter
public enum TokenType {
    /**
     * Access Token：短期有效，用于API请求认证
     */
    ACCESS("access", 15 * 60 * 1000L),

    /**
     * Refresh Token：长期有效，用于刷新Access Token
     */
    REFRESH("refresh", 7 * 24 * 60 * 60 * 1000L);

    /**
     * Token类型标识（存储在JWT claims中）
     */
    private final String type;

    /**
     * Token过期时间（毫秒）
     */
    private final Long expiration;

    TokenType(String type, Long expiration) {
        this.type = type;
        this.expiration = expiration;
    }

    /**
     * 根据类型标识获取TokenType枚举
     *
     * @param type 类型标识
     * @return TokenType枚举，未找到返回null
     */
    public static TokenType fromType(String type) {
        for (TokenType tokenType : values()) {
            if (tokenType.getType().equals(type)) {
                return tokenType;
            }
        }
        return null;
    }
}
