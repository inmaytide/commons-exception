package com.inmaytide.exception.http.domain;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ResponseBody implements Serializable {

    private Long timestamp;

    private HttpStatus status;

    private String error;

    private String message;

    private String url;

    ResponseBody() {
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
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
        fields.add(String.format("\"%s\":\"%s\"", "error", getError()));
        fields.add(String.format("\"%s\":%d", "status", getStatus().value()));
        fields.add(String.format("\"%s\":\"%s\"", "message", getMessage()));
        fields.add(String.format("\"%s\":\"%s\"", "path", getUrl()));
        return "{" + StringUtils.collectionToDelimitedString(fields, ",") + "}";
    }

    public static ResponseBodyBuilder newBuilder() {
        return new ResponseBodyBuilder();
    }


}
