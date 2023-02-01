package com.inmaytide.exception.web.domain;

import com.inmaytide.exception.web.HttpResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author inmaytide
 * @since 2020/11/25
 */
public class DefaultResponse implements Response {

    @Serial
    private static final long serialVersionUID = -7223558303498061347L;

    private Instant timestamp;

    private Integer status;

    private String code;

    private String message;

    private String url;

    private List<String> placeholders;

    private DefaultResponse() {

    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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

    public List<String> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(List<String> placeholders) {
        this.placeholders = placeholders;
    }

    public static ResponseBuilder builder() {
        return new ResponseBuilder();
    }

    public static ResponseBuilder withException(HttpResponseException e) {
        return builder().exception(e);
    }

    @Override
    public String toString() {
        String values = Stream.of(DefaultResponse.class.getDeclaredFields())
                .peek(AccessibleObject::trySetAccessible)
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
        if (value instanceof List<?> values) {
            return Optional.of(
                    String.format(
                            "\"%s\":%s",
                            field.getName(),
                            "[" + values.stream().map(e -> Objects.toString(e, "")).collect(Collectors.joining("\", \"")) + "]"
                    )
            );
        }
        return Optional.of(String.format("\"%s\": \"%s\"", field.getName(), value));
    }

    public static class ResponseBuilder {

        private HttpResponseException exception;

        private String url;

        private String message;

        private List<String> placeholders;

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

        public ResponseBuilder placeholders(String... placeholders) {
            this.placeholders = Arrays.asList(placeholders);
            return this;
        }

        public DefaultResponse build() {
            DefaultResponse response = new DefaultResponse();
            response.setUrl(url);
            response.setTimestamp(exception == null ? Instant.now() : exception.getTimestamp());
            response.setCode(StringUtils.hasText(code) ? code : (exception == null ? null : exception.getCode()));
            response.setMessage(StringUtils.hasText(message) ? message : (exception == null ? null : exception.getMessage()));
            response.setStatus(exception == null ? HttpStatus.INTERNAL_SERVER_ERROR.value() : exception.getStatus().value());
            response.setPlaceholders(exception == null ? placeholders : Arrays.asList(exception.getPlaceholders()));
            return response;
        }
    }
}
