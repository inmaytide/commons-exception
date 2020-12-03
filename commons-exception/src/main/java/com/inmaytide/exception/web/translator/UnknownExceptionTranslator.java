package com.inmaytide.exception.web.translator;

import com.inmaytide.exception.translator.ThrowableTranslator;
import com.inmaytide.exception.web.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author luomiao
 * @since 2020/11/26
 */
public class UnknownExceptionTranslator implements ThrowableTranslator<HttpResponseException> {

    private static final Logger log = LoggerFactory.getLogger(UnknownExceptionTranslator.class);

    public static final int ORDER = 100;

    @Override
    public Optional<HttpResponseException> translate(Throwable e) {
        log.error("Unknown excpetion: ", e);
        return Optional.of(new HttpResponseException(e));
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
