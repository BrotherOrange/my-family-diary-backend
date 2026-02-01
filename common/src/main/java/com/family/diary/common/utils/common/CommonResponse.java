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

package com.family.diary.common.utils.common;

import com.family.diary.common.enums.errors.ResponseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

/**
 * 统一返回体格式
 *
 * @param <T> 任意类型返回体
 * @author Richard Zhang
 * @since 2025-07-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应状态
     */
    private Boolean success;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 错误信息
     */
    private String errors;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应成功
     *
     * @return CommonResponse<T>
     */
    public static <T> ResponseEntity<CommonResponse<T>> ok(T data) {
        return buildCommonResponse(200, true, "操作成功", null, data);
    }

    /**
     * 响应失败
     *
     * @param errors errors
     * @return CommonResponse<T>
     */
    public static <T> ResponseEntity<CommonResponse<T>> fail(String errors) {
        return fail(ResponseErrorCode.INTERNAL_SERVER_ERROR, errors);
    }

    /**
     * 响应失败
     *
     * @param errors  errors
     * @param message 错误消息
     * @return CommonResponse<T>
     */
    public static <T> ResponseEntity<CommonResponse<T>> fail(String errors, String message) {
        return fail(ResponseErrorCode.INTERNAL_SERVER_ERROR, message, errors);
    }

    /**
     * 响应失败
     *
     * @param errorCode 错误码
     * @param errors    errors
     * @return CommonResponse<T>
     */
    public static <T> ResponseEntity<CommonResponse<T>> fail(ResponseErrorCode errorCode, String errors) {
        return buildCommonResponse(errorCode.getCode(), false, errorCode.getMessage(), errors, null);
    }

    /**
     * 响应失败
     *
     * @param errorCode 错误码
     * @param message   错误消息
     * @param errors    errors
     * @return CommonResponse<T>
     */
    public static <T> ResponseEntity<CommonResponse<T>> fail(
            ResponseErrorCode errorCode, String message, String errors) {
        return buildCommonResponse(errorCode.getCode(), false, message, errors, null);
    }

    private static <T> ResponseEntity<CommonResponse<T>> buildCommonResponse(
            int code, Boolean success, String message, String errors, T data) {
        return ResponseEntity.status(code).body(new CommonResponse<>(code, success, message, errors, data));
    }
}
