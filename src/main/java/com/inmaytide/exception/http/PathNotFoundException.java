package com.inmaytide.exception.http;

import org.springframework.http.HttpStatus;

/**
 * @author luomiao
 * @since 2020/11/25
 */
public class PathNotFoundException extends HttpResponseException {

    public PathNotFoundException(Throwable e) {
        super(e);
    }

    @Override
    public HttpStatus getDefaultStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getDefaultCode() {
        return "exception_path_notfound";
    }

}
