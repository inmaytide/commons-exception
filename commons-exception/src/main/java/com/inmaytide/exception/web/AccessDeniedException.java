package com.inmaytide.exception.web;

import org.springframework.http.HttpStatus;

/**
 * 已登录但是没有权限访问资源
 *
 * @author luomiao
 * @since 2020/12/01
 */
public class AccessDeniedException extends HttpResponseException {

    private static final String DEFAULT_CODE = "exception_access_denied";

    public AccessDeniedException() {
        super(HttpStatus.FORBIDDEN, DEFAULT_CODE);
    }

    public AccessDeniedException(String code) {
        super(HttpStatus.FORBIDDEN, code);
    }

    public AccessDeniedException(Throwable cause) {
        super(HttpStatus.FORBIDDEN, DEFAULT_CODE, cause);
    }

}
