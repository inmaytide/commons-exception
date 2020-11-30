package com.inmaytide.exception.web;

import org.springframework.http.HttpStatus;

/**
 * @author luomiao
 * @since 2020/11/26
 */
public class BadCredentialsException extends HttpResponseException {

    public BadCredentialsException() {
        super();
    }

    public BadCredentialsException(Throwable cause) {
        super(cause);
    }


    @Override
    public HttpStatus getDefaultStatus() {
        return HttpStatus.FORBIDDEN;
    }

    @Override
    public String getDefaultCode() {
        return "exception_bad_credentials";
    }

}
