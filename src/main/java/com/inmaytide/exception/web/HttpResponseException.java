package com.inmaytide.exception.web;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.time.Instant;

/**
 * @author luomiao
 * @since 2020/11/25
 */
public class HttpResponseException extends RuntimeException {

    private final static HttpStatus DEFAULT_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    private final static String DEFAULT_CODE = "exception_unknown";

    private final Instant timestamp = Instant.now();

    private String code;

    private HttpStatus status;

    public HttpResponseException() {
    }

    public HttpResponseException(String code) {
        this.code = code;
    }

    public HttpResponseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public HttpResponseException(Throwable cause) {
        super(cause.getClass().getName() + ": " + cause.getMessage(), cause);
    }

    public HttpResponseException(HttpStatus status, Throwable cause) {
        this(cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status == null ? getDefaultStatus() : status;
    }

    public HttpStatus getDefaultStatus() {
        return DEFAULT_STATUS;
    }

    public String getDefaultCode() {
        return DEFAULT_CODE;
    }

    public String getCode() {
        return StringUtils.hasText(code) ? code : getDefaultCode();
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String getMessage() {
        return StringUtils.hasText(super.getMessage()) ? super.getMessage() : getStatus().getReasonPhrase();
    }

}
