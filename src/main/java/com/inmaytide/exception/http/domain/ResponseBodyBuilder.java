package com.inmaytide.exception.http.domain;

import com.inmaytide.exception.http.HttpResponseException;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author luomiao
 * @since 2019/09/05
 */
public class ResponseBodyBuilder {

    private Throwable throwable;

    private String url;

    ResponseBodyBuilder() {

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
        instance.setError(e.getCode());
        instance.setTimestamp(e.getTimestamp());
        return instance;
    }
}