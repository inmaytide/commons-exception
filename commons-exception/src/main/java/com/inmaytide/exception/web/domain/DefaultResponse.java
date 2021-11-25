package com.inmaytide.exception.web.domain;

import com.inmaytide.exception.web.HttpResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author luomiao
 * @since 2020/11/25
 */
public class DefaultResponse implements Serializable, Response {

    private Instant timestamp;

    private HttpStatus status;

    private String code;

    private String message;

    private String url;

    private DefaultResponse() {

    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static ResponseBuilder builder() {
        return new ResponseBuilder();
    }

    public static ResponseBuilder withException(HttpResponseException e) {
        return builder().exception(e);
    }

    @Override
    public String toString() {
        return "DefaultResponse{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public static class ResponseBuilder {

        private HttpResponseException exception;

        private String url;

        private String message;

        private String code;

        ResponseBuilder() {

        }

        public ResponseBuilder exception(HttpResponseException e) {
            this.exception = e;
            return this;
        }

        public ResponseBuilder URL(String url) {
            this.url = url;
            return this;
        }

        public ResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ResponseBuilder code(String code) {
            this.code = code;
            return this;
        }

        public DefaultResponse build() {
            DefaultResponse response = new DefaultResponse();
            response.setUrl(url);
            response.setTimestamp(exception == null ? Instant.now() : exception.getTimestamp());
            response.setCode(StringUtils.hasText(code) ? code : (exception == null ? null : exception.getCode()));
            response.setMessage(StringUtils.hasText(message) ? message : (exception == null ? null : exception.getMessage()));
            response.setStatus(exception == null ? HttpStatus.INTERNAL_SERVER_ERROR : exception.getStatus());
            return response;
        }
    }
}
