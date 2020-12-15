package com.inmaytide.exception.web;

import org.springframework.http.HttpStatus;

/**
 * 请求参数验证错误
 *
 * @author luomiao
 * @since 2020/11/25
 */
public class BadRequestException extends HttpResponseException {

    private static final String DEFAULT_CODE = "exception_bad_request";

    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST, DEFAULT_CODE);
    }

    public BadRequestException(String code) {
        super(HttpStatus.BAD_REQUEST, code);
    }

    public BadRequestException(Throwable cause) {
        super(HttpStatus.BAD_REQUEST, DEFAULT_CODE, cause);
    }
}
