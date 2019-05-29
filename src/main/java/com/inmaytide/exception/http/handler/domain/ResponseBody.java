package com.inmaytide.exception.http.handler.domain;

import com.inmaytide.exception.http.HttpResponseException;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class ResponseBody implements Serializable {

    private Long timestamp;

    private Integer httpStatus;

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

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
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
        return "{" +
                String.format("\"timestamp\": \"%s\", ", getTimestamp()) +
                String.format("\"code\": \"%s\", ", getCode()) +
                String.format("\"status\": \"%s\", ", getHttpStatus()) +
                String.format("\"message\": \"%s\", ", getMessage()) +
                String.format("\"path\": \"%s\"", getUrl()) +
                "}";
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
            instance.setHttpStatus(e.getStatus().value());
            instance.setCode(e.getCode());
            instance.setTimestamp(e.getTimestamp());
            return instance;
        }


    }

}
