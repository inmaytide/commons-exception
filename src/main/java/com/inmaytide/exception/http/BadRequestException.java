package com.inmaytide.exception.http;

import org.springframework.http.HttpStatus;

/**
 * @author luomiao
 * @since 2020/11/25
 */
public class BadRequestException extends HttpResponseException {

    @Override
    public HttpStatus getDefaultStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getDefaultCode() {
        return "exception_bad_request";
    }
}
