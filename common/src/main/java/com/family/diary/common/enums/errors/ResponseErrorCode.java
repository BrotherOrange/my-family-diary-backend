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
    // ==================== 4xx 客户端错误 ====================
    BAD_REQUEST(400, "非法请求", HttpStatus.SC_BAD_REQUEST),
    UNAUTHORIZED(401, "未授权", HttpStatus.SC_UNAUTHORIZED),
    PAYMENT_REQUIRED(402, "需要付款", HttpStatus.SC_PAYMENT_REQUIRED),
    FORBIDDEN(403, "禁止访问", HttpStatus.SC_FORBIDDEN),
    NOT_FOUND(404, "资源不存在", HttpStatus.SC_NOT_FOUND),
    METHOD_NOT_ALLOWED(405, "请求方法不允许", HttpStatus.SC_METHOD_NOT_ALLOWED),
    NOT_ACCEPTABLE(406, "不可接受的请求", HttpStatus.SC_NOT_ACCEPTABLE),
    REQUEST_TIMEOUT(408, "请求超时", HttpStatus.SC_REQUEST_TIMEOUT),
    CONFLICT(409, "资源冲突", HttpStatus.SC_CONFLICT),
    GONE(410, "资源已删除", HttpStatus.SC_GONE),
    PRECONDITION_FAILED(412, "前置条件失败", HttpStatus.SC_PRECONDITION_FAILED),
    PAYLOAD_TOO_LARGE(413, "请求体过大", HttpStatus.SC_REQUEST_TOO_LONG),
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒体类型", HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE),
    UNPROCESSABLE_ENTITY(422, "无法处理的实体", HttpStatus.SC_UNPROCESSABLE_ENTITY),
    LOCKED(423, "资源已锁定", HttpStatus.SC_LOCKED),
    TOO_MANY_REQUESTS(429, "请求过于频繁", 429),

    // ==================== 5xx 服务端错误 ====================
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
