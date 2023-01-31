package com.inmaytide.exception.web;

import org.springframework.http.HttpStatus;

/**
 * 请求的服务不可用
 *
 * @author luomiao
 * @since 2020/11/26
 */
public class ServiceUnavailableException extends HttpResponseException {

    private static final long serialVersionUID = -372994981996874593L;

    private static final String DEFAULT_CODE = "exception_service_unavailable";

    public ServiceUnavailableException() {
        super(HttpStatus.SERVICE_UNAVAILABLE, DEFAULT_CODE);
    }

    public ServiceUnavailableException(String code) {
        super(HttpStatus.SERVICE_UNAVAILABLE, code);
    }

    public ServiceUnavailableException(Throwable cause) {
        super(HttpStatus.SERVICE_UNAVAILABLE, DEFAULT_CODE, cause);
    }

}
