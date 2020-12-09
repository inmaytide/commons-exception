package com.inmaytide.exception.web;

import org.springframework.http.HttpStatus;

/**
 * @author luomiao
 * @since 2020/11/25
 */
public class BadRequestException extends HttpResponseException {

    public BadRequestException(String code) {
        super(code);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }

    @Override
    public HttpStatus getDefaultStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getDefaultCode() {
        return "exception_bad_request";
    }
}
