package com.inmaytide.exception.web;

import org.springframework.http.HttpStatus;

/**
 * @author luomiao
 * @since 2020/12/01
 */
public class AccessDeniedException extends HttpResponseException {

    public AccessDeniedException() {
        super();
    }

    public AccessDeniedException(Throwable cause) {
        super(cause);
    }

    @Override
    public HttpStatus getDefaultStatus() {
        return HttpStatus.FORBIDDEN;
    }

    @Override
    public String getDefaultCode() {
        return "exception_access_denied";
    }

}
