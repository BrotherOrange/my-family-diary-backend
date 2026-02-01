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
import org.apache.http.HttpStatus;

/**
 * 异常错误码枚举类
 *
 * @author Richard Zhang
 * @since 2025-07-12
 */
@Getter
public enum ResponseErrorCode {
    BAD_REQUEST(400, "非法请求", HttpStatus.SC_BAD_REQUEST),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误", HttpStatus.SC_INTERNAL_SERVER_ERROR),
    NOT_IMPLEMENTED(501, "功能未实现", HttpStatus.SC_NOT_IMPLEMENTED),
    BAD_GATEWAY(502, "错误网关", HttpStatus.SC_BAD_GATEWAY),
    SERVICE_UNAVAILABLE(503, "服务不可用", HttpStatus.SC_SERVICE_UNAVAILABLE),
    GATEWAY_TIMEOUT(504, "网关超时", HttpStatus.SC_GATEWAY_TIMEOUT),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP版本不支持", HttpStatus.SC_HTTP_VERSION_NOT_SUPPORTED),
    INSUFFICIENT_STORAGE(507, "存储空间不足", HttpStatus.SC_INSUFFICIENT_STORAGE),
    ;

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误信息
     */
    private final String message;

    /**
     * HttpStatus 错误码
     */
    private final int httpStatus;

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param message 错误信息
     */
    ResponseErrorCode(Integer code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
