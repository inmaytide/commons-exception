package com.inmaytide.exception.http.domain;

import com.inmaytide.exception.http.HttpResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResponseBody implements Serializable {

    private Long timestamp;

    private HttpStatus status;

    private String code;

    private String message;

    private String url;

    private ResponseBody() {
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
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

    @Override
    public String toString() {
        List<String> fields = new ArrayList<>(5);
        fields.add(String.format("\"%s\":%d", "timestamp", getTimestamp()));
        fields.add(String.format("\"%s\":\"%s\"", "code", getCode()));
        fields.add(String.format("\"%s\":%d", "status", getStatus().value()));
        fields.add(String.format("\"%s\":\"%s\"", "message", getMessage()));
        fields.add(String.format("\"%s\":\"%s\"", "path", getUrl()));
        return "{" + StringUtils.collectionToDelimitedString(fields, ",") + "}";
    }

    public static ResponseBodyBuilder newBuilder() {
        return new ResponseBodyBuilder();
    }

    public static class ResponseBodyBuilder {

        private Throwable throwable;

        private String url;

        private ResponseBodyBuilder() {

        }

        public ResponseBodyBuilder throwable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        public ResponseBodyBuilder url(String url) {
            this.url = url;
            return this;
        }

        public ResponseBody build() {
            Assert.notNull(throwable, "There is no exception instance supplied");
            HttpResponseException e = Optional.of(throwable)
                    .filter(ex -> ex instanceof HttpResponseException)
                    .map(ex -> (HttpResponseException) ex)
                    .orElse(new HttpResponseException(throwable));

            ResponseBody instance = new ResponseBody();
            instance.setUrl(this.url);
            instance.setMessage(throwable.getMessage());
            instance.setStatus(e.getStatus());
            instance.setCode(e.getCode());
            instance.setTimestamp(e.getTimestamp());
            return instance;
        }
    }

}
