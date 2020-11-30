package com.inmaytide.exception.web.translator;

import com.inmaytide.exception.web.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author luomiao
 * @since 2020/11/26
 */
public class UnknownExceptionTranslator extends AbstractHttpExceptionTranslator {

    private static final Logger log = LoggerFactory.getLogger(UnknownExceptionTranslator.class);

    public static final int ORDER = 100;

    @Override
    public Optional<HttpResponseException> translate(Throwable throwable) {
        log.error("Unknow excpetion: ", throwable);
        return Optional.of(new HttpResponseException(throwable));
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
