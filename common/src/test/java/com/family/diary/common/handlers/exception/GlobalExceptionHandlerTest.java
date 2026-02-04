package com.family.diary.common.handlers.exception;

import com.family.diary.common.enums.errors.ResponseErrorCode;
import com.family.diary.common.exceptions.UnauthorizedException;
import com.family.diary.common.utils.common.CommonResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    @Test
    void handleBaseExceptionSetsBizCode() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        UnauthorizedException ex = new UnauthorizedException("unauthorized");

        ResponseEntity<CommonResponse<Object>> response = handler.handleBaseException(ex);

        assertEquals(ResponseErrorCode.UNAUTHORIZED.getCode(), response.getBody().getCode());
        assertEquals(ex.getErrorCode().getCode(), response.getBody().getBizCode());
        assertEquals(ResponseErrorCode.UNAUTHORIZED.getHttpStatus(), response.getStatusCode().value());
    }

    @Test
    void handleUnknownExceptionReturnsInternalServerError() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<CommonResponse<Object>> response = handler.handleUnknownException(new RuntimeException("boom"));

        assertNotNull(response.getBody());
        assertEquals(ResponseErrorCode.INTERNAL_SERVER_ERROR.getCode(), response.getBody().getCode());
        assertEquals(ResponseErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus(), response.getStatusCode().value());
    }
}
