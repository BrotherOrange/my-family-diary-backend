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

package com.family.diary.common.constants.common;

/**
 * JWT常量类
 *
 * @author Richard Zhang
 * @since 2025-08-21
 * @see com.family.diary.common.enums.jwt.TokenType Token类型及过期时间配置
 */
public interface JWTConstants {
    /**
     * Token类型claim名称
     */
    String CLAIM_TOKEN_TYPE = "token_type";
}
