package com.inmaytide.exception.web.translator;

import com.inmaytide.exception.web.HttpResponseException;

import java.util.Optional;

/**
 * @author inmaytide
 * @since 2020/11/26
 */
public class HttpResponseExceptionTranslator extends AbstractHttpExceptionTranslator {

    public static final int ORDER = 10;

    @Override
    public Optional<HttpResponseException> execute(Throwable e) {
        if (e instanceof HttpResponseException) {
            return Optional.of((HttpResponseException) e);
        }
        return Optional.empty();
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

}
