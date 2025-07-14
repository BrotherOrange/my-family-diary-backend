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

package com.family.diary.common.enums.errors;

import lombok.Getter;

/**
 * 异常错误码枚举
 *
 * @author Richard Zhang
 * @since 2025-07-12
 */
@Getter
public enum ExceptionErrorCode {
    UNKNOWN_ERROR("ERR-0001", "未知错误"),
    COMMON_ERROR("ERR-0002", "通用错误"),
    INVALID_PARAM("ERR-0003", "参数无效"),
    UNAUTHORIZED("ERR-0004", "未授权"),
    FORBIDDEN("ERR-0005", "禁止访问"),
    NOT_FOUND("ERR-0006", "资源不存在"),
    TIMEOUT("ERR-0007", "操作超时");

    /**
     * 错误码
     */
    private final String code;

    /**
     * 错误消息
     */
    private final String message;

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param message 错误消息
     */
    ExceptionErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
