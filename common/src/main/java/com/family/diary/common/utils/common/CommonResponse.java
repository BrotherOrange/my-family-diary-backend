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
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
     * 业务错误码
     */
    private String bizCode;

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
        return buildCommonResponse(200, 200, true, "操作成功", null, null, data);
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
        return buildCommonResponse(errorCode.getHttpStatus(), errorCode.getCode(), false,
                errorCode.getMessage(), errors, null, null);
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
        return buildCommonResponse(errorCode.getHttpStatus(), errorCode.getCode(), false,
                message, errors, null, null);
    }

    /**
     * 响应失败（带业务码）
     *
     * @param errorCode 错误码
     * @param message   错误消息
     * @param errors    errors
     * @param bizCode   业务错误码
     * @return CommonResponse<T>
     */
    public static <T> ResponseEntity<CommonResponse<T>> fail(
            ResponseErrorCode errorCode, String message, String errors, String bizCode) {
        return buildCommonResponse(errorCode.getHttpStatus(), errorCode.getCode(), false,
                message, errors, bizCode, null);
    }

    private static <T> ResponseEntity<CommonResponse<T>> buildCommonResponse(
            int httpStatus, Integer code, Boolean success, String message, String errors, String bizCode, T data) {
        return ResponseEntity.status(httpStatus).body(new CommonResponse<>(code, bizCode, success, message, errors, data));
    }

    // ==================== Filter 响应写入方法 ====================

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 在 Filter 中写入失败响应（用于无法使用 ResponseEntity 的场景）
     *
     * @param response  HttpServletResponse
     * @param errorCode 错误码枚举
     * @param message   错误消息
     * @param data      响应数据
     * @throws IOException IO异常
     */
    public static <T> void writeErrorResponse(HttpServletResponse response, ResponseErrorCode errorCode,
                                              String message, T data) throws IOException {
        response.setStatus(errorCode.getHttpStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        CommonResponse<T> body = new CommonResponse<>(
                errorCode.getCode(),
                null,
                false,
                message,
                null,
                data
        );
        response.getWriter().write(OBJECT_MAPPER.writeValueAsString(body));
    }

    /**
     * 在 Filter 中写入失败响应（无数据）
     *
     * @param response  HttpServletResponse
     * @param errorCode 错误码枚举
     * @param message   错误消息
     * @throws IOException IO异常
     */
    public static void writeErrorResponse(HttpServletResponse response, ResponseErrorCode errorCode,
                                          String message) throws IOException {
        writeErrorResponse(response, errorCode, message, null);
    }
}
