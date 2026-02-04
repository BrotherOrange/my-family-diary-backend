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

package com.family.diary.common.handlers.exception;

import com.family.diary.common.enums.errors.ExceptionErrorCode;
import com.family.diary.common.enums.errors.ResponseErrorCode;
import com.family.diary.common.exceptions.BaseException;
import com.family.diary.common.exceptions.ConflictException;
import com.family.diary.common.exceptions.UnauthorizedException;
import com.family.diary.common.exceptions.database.InsertException;
import com.family.diary.common.exceptions.database.QueryException;
import com.family.diary.common.exceptions.mapper.MapperException;
import com.family.diary.common.utils.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author Richard Zhang
 * @since 2026-02-04
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<CommonResponse<Object>> handleBaseException(BaseException ex) {
        logError(ex);
        var responseError = mapBaseException(ex);
        String bizCode = ex.getErrorCode() != null ? ex.getErrorCode().getCode() : null;
        return CommonResponse.fail(responseError, ex.getMessage(), null, bizCode);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<CommonResponse<Object>> handleDuplicateKey(DuplicateKeyException ex) {
        logError(ex);
        return CommonResponse.fail(ResponseErrorCode.CONFLICT, "资源已存在，无法重复创建");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Object>> handleUnknownException(Exception ex) {
        logError(ex);
        toUnknownException(ex);
        return CommonResponse.fail(ResponseErrorCode.INTERNAL_SERVER_ERROR,
                ResponseErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    private ResponseErrorCode mapBaseException(BaseException ex) {
        if (ex instanceof ConflictException) {
            return ResponseErrorCode.CONFLICT;
        }
        if (ex instanceof UnauthorizedException) {
            return ResponseErrorCode.UNAUTHORIZED;
        }
        if (ex instanceof QueryException) {
            return ResponseErrorCode.NOT_FOUND;
        }
        if (ex instanceof MapperException) {
            return ResponseErrorCode.BAD_REQUEST;
        }
        if (ex instanceof InsertException) {
            return ResponseErrorCode.INTERNAL_SERVER_ERROR;
        }

        ExceptionErrorCode errorCode = ex.getErrorCode();
        if (errorCode == null) {
            return ResponseErrorCode.INTERNAL_SERVER_ERROR;
        }
        return switch (errorCode) {
            case INVALID_PARAM -> ResponseErrorCode.BAD_REQUEST;
            case UNAUTHORIZED -> ResponseErrorCode.UNAUTHORIZED;
            case FORBIDDEN -> ResponseErrorCode.FORBIDDEN;
            case NOT_FOUND -> ResponseErrorCode.NOT_FOUND;
            case TIMEOUT -> ResponseErrorCode.REQUEST_TIMEOUT;
            case CONFLICT -> ResponseErrorCode.CONFLICT;
            case COMMON_ERROR, UNKNOWN_ERROR -> ResponseErrorCode.INTERNAL_SERVER_ERROR;
        };
    }
}
