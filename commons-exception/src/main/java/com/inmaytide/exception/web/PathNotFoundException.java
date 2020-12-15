package com.inmaytide.exception.web;

import org.springframework.http.HttpStatus;

/**
 * 请求的接口路径没有找到
 *
 * @author luomiao
 * @since 2020/11/25
 */
public class PathNotFoundException extends HttpResponseException {

    private static final String DEFAULT_CODE = "exception_path_notfound";

    public PathNotFoundException() {
        super(HttpStatus.NOT_FOUND, DEFAULT_CODE);
    }

    public PathNotFoundException(String code) {
        super(HttpStatus.NOT_FOUND, code);
    }

    public PathNotFoundException(Throwable cause) {
        super(HttpStatus.NOT_FOUND, DEFAULT_CODE, cause);
    }

}
