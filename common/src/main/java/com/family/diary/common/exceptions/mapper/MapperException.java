package com.family.diary.common.exceptions.mapper;

import com.family.diary.common.enums.errors.ExceptionErrorCode;
import com.family.diary.common.exceptions.BaseException;

/**
 * 映射类异常
 *
 * @author Richard Zhang
 * @since 2025-08-22
 */
public class MapperException extends BaseException {
    /**
     * 构造函数
     *
     * @param message 错误信息
     */
    public MapperException(String message) {
        super(ExceptionErrorCode.INVALID_PARAM, message);
    }
}
