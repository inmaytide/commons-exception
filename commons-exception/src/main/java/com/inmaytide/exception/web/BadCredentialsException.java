package com.inmaytide.exception.web;

import org.springframework.http.HttpStatus;

/**
 * 登录时账号密码错误或者访问资源时凭证已失效
 *
 * @author luomiao
 * @since 2020/11/26
 */
public class BadCredentialsException extends HttpResponseException {

    private static final long serialVersionUID = 3394677497571389704L;

    private static final String DEFAULT_CODE = "exception_bad_credentials";

    public BadCredentialsException() {
        super(HttpStatus.FORBIDDEN, DEFAULT_CODE);
    }

    public BadCredentialsException(String code) {
        super(HttpStatus.FORBIDDEN, code);
    }

    public BadCredentialsException(Throwable cause) {
        super(HttpStatus.FORBIDDEN, DEFAULT_CODE, cause);
    }

}
