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

    /**
     * 异常触发时间
     */
    private final Instant timestamp;

    /**
     * 异常消息编码，通常用于返回前端获取对应语言的消息内容
     */
    private final String code;

    /**
     * 异常http状态, 默认500 {@link HttpStatus.INTERNAL_SERVER_ERROR}
     */
    private final HttpStatus status;

    public HttpResponseException() {
        super(DEFAULT_STATUS.getReasonPhrase());
        this.timestamp = Instant.now();
        this.status = DEFAULT_STATUS;
        this.code = DEFAULT_CODE;
    }

    public HttpResponseException(HttpStatus status, String code, String reason, Throwable cause) {
        super(reason, cause);
        this.timestamp = Instant.now();
        this.status = status != null ? status : DEFAULT_STATUS;
        this.code = StringUtils.hasText(code) ? code : DEFAULT_CODE;
    }

    public HttpResponseException(HttpStatus status, String code, String reason) {
        this(status, code, reason, null);
    }

    public HttpResponseException(HttpStatus status, String code, Throwable cause) {
        this(status, code, null, cause);
    }

    public HttpResponseException(Throwable cause) {
        this(null, null, null, cause);
    }

    public HttpResponseException(HttpStatus status, String code) {
        this(status, code, null, null);
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        if (StringUtils.hasText(super.getMessage())) {
            return super.getMessage();
        }
        if (getCause() != null) {
            String message = getCause().getClass().getName();
            if (StringUtils.hasText(getCause().getMessage())) {
                message += ": " + getCause().getMessage();
            }
            return message;
        }
        return getStatus().getReasonPhrase();
    }


}
