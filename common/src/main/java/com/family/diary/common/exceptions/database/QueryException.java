package com.family.diary.common.exceptions.database;

import com.family.diary.common.enums.errors.ExceptionErrorCode;
import com.family.diary.common.exceptions.BaseException;

/**
 * 数据查询异常类
 *
 * @author Richard Zhang
 * @since 2025-08-21
 */
public class QueryException extends BaseException {
    /**
     * 构造函数
     *
     * @param message 异常信息
     */
    public QueryException(String message) {
        super(ExceptionErrorCode.NOT_FOUND, message);
    }
}
