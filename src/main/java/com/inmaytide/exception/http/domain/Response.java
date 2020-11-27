package com.inmaytide.exception.http.domain;

import com.inmaytide.exception.http.HttpResponseException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author luomiao
 * @since 2020/11/25
 */
public class Response implements Serializable {

    private Instant timestamp;

    private HttpStatus status;

    private String code;

    private String message;

    private String url;

    private Response() {

    }

    public static ResponseBuilder builder() {
        return new ResponseBuilder();
    }

    public static ResponseBuilder withException(HttpResponseException e) {
        return builder().withException(e);
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

    public byte[] asBytes() {
        return toString().getBytes(StandardCharsets.UTF_8);
    }

    public DataBuffer asDataBuffer(DataBufferFactory bufferFactory) {
        return bufferFactory.wrap(asBytes());
    }

    @Override
    public String toString() {
        String values = Stream.of(Response.class.getDeclaredFields())
                .peek(f -> f.setAccessible(true))
                .map(this::fieldValue)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.joining(","));
        return "{" + values + "}";
    }

    private Optional<String> fieldValue(Field field) {
        Object value = ReflectionUtils.getField(field, this);
        if (value == null) {
            return Optional.empty();
        }
        if (value instanceof HttpStatus) {
            return Optional.of(String.format("\"%s\":%d", field.getName(), ((HttpStatus) value).value()));
        }
        return Optional.of(String.format("\"%s\": \"%s\"", field.getName(), value));
    }

    public static class ResponseBuilder {

        private HttpResponseException exception;

        private String url;

        private String message;

        private String code;

        ResponseBuilder() {

        }

        public ResponseBuilder withException(HttpResponseException e) {
            this.exception = e;
            return this;
        }

        public ResponseBuilder withUrl(String url) {
            this.url = url;
            return this;
        }

        public ResponseBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ResponseBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public Response build() {
            Response response = new Response();
            response.setUrl(url);
            response.setTimestamp(exception == null ? Instant.now() : exception.getTimestamp());
            response.setCode(StringUtils.hasText(code) ? code : (exception == null ? null : exception.getCode()));
            response.setMessage(StringUtils.hasText(message) ? message : (exception == null ? null : exception.getMessage()));
            response.setStatus(exception == null ? HttpStatus.INTERNAL_SERVER_ERROR : exception.getStatus());
            return response;
        }
    }
}
