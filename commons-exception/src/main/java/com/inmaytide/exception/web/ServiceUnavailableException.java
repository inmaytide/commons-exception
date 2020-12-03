package com.inmaytide.exception.web;

import org.springframework.http.HttpStatus;

/**
 * @author luomiao
 * @since 2020/11/26
 */
public class ServiceUnavailableException extends HttpResponseException {

    public ServiceUnavailableException(Throwable e) {
        super(e);
    }

    @Override
    public HttpStatus getDefaultStatus() {
        return HttpStatus.SERVICE_UNAVAILABLE;
    }

    @Override
    public String getDefaultCode() {
        return "exception_service_unavailable";
    }

}
