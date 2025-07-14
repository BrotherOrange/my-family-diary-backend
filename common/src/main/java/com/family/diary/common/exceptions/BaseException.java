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

package com.family.diary.common.exceptions;

import com.family.diary.common.enums.errors.ExceptionErrorCode;

/**
 * 基础异常类，所有自定义异常都应继承此类
 *
 * @author Richard Zhang
 * @since 2025-07-12
 */
public class BaseException extends RuntimeException {
    /**
     * 错误码
     */
    private final ExceptionErrorCode errorCode;

    /**
     * 无参构造方法
     */
    public BaseException() {
        super();
        this.errorCode = ExceptionErrorCode.UNKNOWN_ERROR;
    }

    /**
     * 带错误消息的构造方法
     *
     * @param message message
     */
    public BaseException(String message) {
        super(message);
        this.errorCode = ExceptionErrorCode.COMMON_ERROR;
    }

    /**
     * 带错误消息和错误码的构造方法
     *
     * @param errorCode errorCode
     * @param message   message
     */
    public BaseException(ExceptionErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * 带错误消息和 Throwable 的构造方法
     *
     * @param message message
     * @param cause   cause
     */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = ExceptionErrorCode.COMMON_ERROR;
    }

    /**
     * 带错误码、错误消息和 Throwable 的构造方法
     *
     * @param errorCode errorCode
     * @param message   message
     * @param cause     cause
     */
    public BaseException(ExceptionErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * 获取错误码
     *
     * @return ExceptionErrorCode
     */
    public ExceptionErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return "BaseException{"
                + "errorCode='"
                + errorCode.getCode()
                + " "
                + errorCode.getMessage()
                + '\''
                + ", message='"
                + getMessage()
                + '\''
                + '}';
    }
}
