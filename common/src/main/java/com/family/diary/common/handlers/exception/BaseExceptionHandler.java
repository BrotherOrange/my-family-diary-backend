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
import com.family.diary.common.exceptions.BaseException;
import com.family.diary.common.models.common.exception.ErrorMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 通用异常处理基类，不绑定到具体的异常处理器注解
 *
 * @author Richard Zhang
 * @since 2025-07-28
 */
@Slf4j
public abstract class BaseExceptionHandler {

    /**
     * 通用的错误响应结构
     *
     * @param message 错误消息
     * @param details 错误详情
     * @return 包含错误信息的Map
     */
    protected ErrorMap buildErrorMap(String message, Object details) {
        return new ErrorMap(message, details);
    }

    /**
     * 记录异常日志
     *
     * @param ex 异常对象
     */
    protected void logError(Exception ex) {
        // 你可以使用日志框架（如 SLF4J）记录异常
        log.error("Exception occurred: {}", String.valueOf(ex));
    }

    /**
     * 处理所有未捕获的异常
     *
     * @param ex 异常对象
     * @return 包含错误信息的响应实体
     */
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception ex) throws BaseException {
        throw new BaseException(ExceptionErrorCode.UNKNOWN_ERROR, ex.getMessage(), ex.getCause());
    }
}
