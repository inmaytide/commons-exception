package com.inmaytide.exception.web;

import org.springframework.http.HttpStatus;

/**
 * @author luomiao
 * @since 2020/11/25
 */
public class ObjectNotFoundException extends HttpResponseException {

    @Override
    public HttpStatus getDefaultStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getDefaultCode() {
        return "exception_obj_notfound";
    }

}
