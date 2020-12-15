package com.inmaytide.exception.web;

import org.springframework.http.HttpStatus;

/**
 * 请求资源时未提供相关凭证
 *
 * @author luomiao
 * @since 2020/11/30
 */
public class UnauthorizedException extends HttpResponseException {

    private static final String DEFAULT_CODE = "exception_unauthorized";

    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED, DEFAULT_CODE);
    }

    public UnauthorizedException(String code) {
        super(HttpStatus.UNAUTHORIZED, code);
    }

    public UnauthorizedException(Throwable cause) {
        super(HttpStatus.UNAUTHORIZED, DEFAULT_CODE, cause);
    }
    
}
