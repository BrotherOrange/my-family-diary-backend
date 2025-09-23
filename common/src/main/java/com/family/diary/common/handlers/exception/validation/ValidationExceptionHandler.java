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

package com.family.diary.common.handlers.exception.validation;

import com.family.diary.common.enums.errors.ResponseErrorCode;
import com.family.diary.common.handlers.exception.BaseExceptionHandler;
import com.family.diary.common.models.common.exception.ErrorMap;
import com.family.diary.common.utils.common.CommonResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler extends BaseExceptionHandler {

    @Override
    public Object handleException(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException) {
            return handleMethodArgumentNotValid((MethodArgumentNotValidException) ex);
        } else if (ex instanceof ConstraintViolationException) {
            return handleConstraintViolation((ConstraintViolationException) ex);
        } else {
            return super.handleException(ex);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<Map<String, Object>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        logError(ex);

        var errors = new HashMap<String, String>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        var errorResponse = buildErrorMap("Method Validation failed", errors);
        return CommonResponse.fail(ResponseErrorCode.BAD_REQUEST, errorResponse.message() + ": " + errors.values());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResponse<Map<String, Object>> handleConstraintViolation(
            ConstraintViolationException ex) {
        logError(ex);

        var errors = new HashMap<String, String>();
        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage()));

        var errorResponse = buildErrorMap("Constraint Validation failed", errors);
        return CommonResponse.fail(ResponseErrorCode.BAD_REQUEST, errorResponse.message() + ": " + errors.values());
    }
}
