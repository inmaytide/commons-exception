package com.inmaytide.exception.web;

import org.springframework.http.HttpStatus;

/**
 * @author luomiao
 * @since 2020/11/30
 */
public class UnauthorizedException extends HttpResponseException {

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(Throwable cause) {
        super(cause);
    }

    @Override
    public HttpStatus getDefaultStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public String getDefaultCode() {
        return "exception_unauthorized";
    }

}
