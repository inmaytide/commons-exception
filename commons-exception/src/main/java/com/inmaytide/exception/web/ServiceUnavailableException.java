package com.inmaytide.exception.web;

import com.inmaytide.exception.web.domain.DefaultErrorCode;
import com.inmaytide.exception.web.domain.ErrorCode;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * 请求的服务无法访问
 *
 * @author inmaytide
 * @since 2020/11/26
 */
public class ServiceUnavailableException extends HttpResponseException {

    @Serial
    private static final long serialVersionUID = -372994981996874593L;

    private static final ErrorCode DEFAULT_CODE = new DefaultErrorCode("exception_service_unavailable", "服务\"{0}\"无法访问");

    public ServiceUnavailableException(String service) {
        super(HttpStatus.SERVICE_UNAVAILABLE, DEFAULT_CODE, service);
    }

    public ServiceUnavailableException(ErrorCode code, String... placeholders) {
        super(HttpStatus.SERVICE_UNAVAILABLE, code, placeholders);
    }

    public ServiceUnavailableException(Throwable cause) {
        super(HttpStatus.SERVICE_UNAVAILABLE, DEFAULT_CODE, cause, "unknown");
    }


}
