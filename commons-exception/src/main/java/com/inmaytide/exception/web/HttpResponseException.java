package com.inmaytide.exception.web;

import com.inmaytide.exception.web.domain.DefaultErrorCode;
import com.inmaytide.exception.web.domain.ErrorCode;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.time.Instant;
import java.util.Objects;

/**
 * 未知异常
 *
 * @author inmaytide
 * @since 2020/11/25
 */
public class HttpResponseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7884478623283660662L;

    private final static HttpStatus DEFAULT_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    private final static ErrorCode DEFAULT_CODE = new DefaultErrorCode("exception_unknown", "未知异常");

    /**
     * 异常触发时间
     */
    private final Instant timestamp;

    /**
     * Http状态吗, 默认 {@link HttpResponseException#DEFAULT_STATUS}
     */
    private final HttpStatus status;

    /**
     * 错误码，通常用于返回前端获取对应语言的错误描述信息
     */
    private final ErrorCode code;

    /**
     * 错误码描述信息中存在占位符时用于替换占位符的值数组
     */
    private final String[] placeholders;

    public HttpResponseException() {
        super(DEFAULT_CODE.description());
        this.timestamp = Instant.now();
        this.status = DEFAULT_STATUS;
        this.code = DEFAULT_CODE;
        this.placeholders = new String[0];
    }

    public HttpResponseException(HttpStatus status, ErrorCode code, Throwable cause, String... placeholders) {
        super(Objects.requireNonNullElse(code, DEFAULT_CODE).getReplacedDescription(placeholders), cause);
        this.timestamp = Instant.now();
        this.status = Objects.requireNonNullElse(status, DEFAULT_STATUS);
        this.code = Objects.requireNonNullElse(code, DEFAULT_CODE);
        this.placeholders = placeholders;
    }

    public HttpResponseException(HttpStatus status, ErrorCode code, String... placeholders) {
        this(status, code, null, placeholders);
    }

    public HttpResponseException(Throwable cause) {
        this(DEFAULT_STATUS, DEFAULT_CODE, cause);
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getCode() {
        return code.value();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String[] getPlaceholders() {
        return placeholders;
    }
}
