package com.inmaytide.exception.http.translator;

import com.inmaytide.exception.http.HttpResponseException;
import com.inmaytide.exception.http.mapper.ResponseStatusExceptionMapper;
import com.inmaytide.exception.translator.ThrowableMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

/**
 * @author luomiao
 * @since 2020/11/26
 */
public class ResponseStatusExceptionTranslator extends AbstractHttpExceptionTranslator {

    public static final int ORDER = 20;

    private static final Logger log = LoggerFactory.getLogger(ResponseStatusExceptionTranslator.class);

    private ThrowableMapper<HttpStatus, Class<? extends HttpResponseException>> throwableMapper;

    public ResponseStatusExceptionTranslator() {
        throwableMapper = new ResponseStatusExceptionMapper();
    }

    public ResponseStatusExceptionTranslator(ThrowableMapper<HttpStatus, Class<? extends HttpResponseException>> throwableMapper) {
        Objects.requireNonNull(throwableMapper);
        this.throwableMapper = throwableMapper;
    }

    @Override
    public Optional<HttpResponseException> translate(Throwable throwable) {
        if (throwable instanceof ResponseStatusException) {
            ResponseStatusException exception = (ResponseStatusException) throwable;
            return throwableMapper.support(exception.getStatus()).map(cls -> super.createInstance(cls, throwable));
        }
        return Optional.empty();
    }

    @Override
    protected Logger getLogger() {
        return log;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
