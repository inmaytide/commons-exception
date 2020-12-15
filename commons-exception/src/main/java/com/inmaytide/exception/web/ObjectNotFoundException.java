package com.inmaytide.exception.web;

import org.springframework.http.HttpStatus;

/**
 * 请求的对象没有找到
 *
 * @author luomiao
 * @since 2020/11/25
 */
public class ObjectNotFoundException extends HttpResponseException {

    private static final String DEFAULT_CODE = "exception_obj_notfound";

    public ObjectNotFoundException() {
        super(HttpStatus.NOT_FOUND, DEFAULT_CODE);
    }

    public ObjectNotFoundException(String code) {
        super(HttpStatus.NOT_FOUND, code);
    }

    public ObjectNotFoundException(Throwable cause) {
        super(HttpStatus.NOT_FOUND, DEFAULT_CODE, cause);
    }

}
