package com.inmaytide.exception.web.translator;

import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.mapper.ResponseStatusExceptionMapper;
import com.inmaytide.exception.translator.ThrowableMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

/**
 * @author inmaytide
 * @since 2020/11/26
 */
public class ResponseStatusExceptionTranslator extends AbstractHttpExceptionTranslator {

    public static final int ORDER = 20;

    private static final Logger log = LoggerFactory.getLogger(ResponseStatusExceptionTranslator.class);

    private final ThrowableMapper<HttpStatusCode, Class<? extends HttpResponseException>> throwableMapper;

    public ResponseStatusExceptionTranslator() {
        throwableMapper = ResponseStatusExceptionMapper.getInstance();
    }

    public ResponseStatusExceptionTranslator(ThrowableMapper<HttpStatusCode, Class<? extends HttpResponseException>> throwableMapper) {
        Objects.requireNonNull(throwableMapper);
        this.throwableMapper = throwableMapper;
    }

    @Override
    public Optional<HttpResponseException> execute(Throwable e) {
        if (e instanceof ResponseStatusException exception) {
            return throwableMapper.support(exception.getStatusCode()).map(cls -> super.createInstance(cls, e));
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
