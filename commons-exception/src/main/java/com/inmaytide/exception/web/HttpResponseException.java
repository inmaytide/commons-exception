package com.inmaytide.exception.web;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.time.Instant;

/**
 * @author luomiao
 * @since 2020/11/25
 */
public class HttpResponseException extends RuntimeException {

    private static final long serialVersionUID = 7884478623283660662L;

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
        Throwable cause = getFinallyCause(this);
        if (cause == this) {
            return StringUtils.hasText(super.getMessage()) ? super.getMessage() : getStatus().getReasonPhrase();
        }
        if (StringUtils.hasText(cause.getMessage())) {
            String message = cause.getClass().getName();
            message += ": " + cause.getMessage();
            return message;
        }
        return getStatus().getReasonPhrase();
    }

    public static Throwable getFinallyCause(Throwable e) {
        if (e.getCause() == null) {
            return e;
        }
        return getFinallyCause(e.getCause());
    }


}
