package com.inmaytide.exception.http.translator;

import com.inmaytide.exception.http.HttpResponseException;

import java.util.Optional;

/**
 * @author luomiao
 * @since 2020/11/26
 */
public class HttpResponseExceptionTranslator extends AbstractHttpExceptionTranslator {

    public static final int ORDER = 10;

    @Override
    public Optional<HttpResponseException> translate(Throwable throwable) {
        if (throwable instanceof HttpResponseException) {
            return Optional.of((HttpResponseException) throwable);
        }
        return Optional.empty();
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

}
