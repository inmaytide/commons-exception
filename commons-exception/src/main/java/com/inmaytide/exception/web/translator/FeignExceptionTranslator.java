package com.inmaytide.exception.web.translator;

import com.inmaytide.exception.translator.ThrowableMapper;
import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.mapper.ResponseStatusExceptionMapper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.Optional;

public class FeignExceptionTranslator extends AbstractHttpExceptionTranslator {

    private static final Logger log = LoggerFactory.getLogger(ResponseStatusExceptionTranslator.class);

    private ThrowableMapper<HttpStatus, Class<? extends HttpResponseException>> throwableMapper;

    public FeignExceptionTranslator() {
        throwableMapper = ResponseStatusExceptionMapper.getInstance();
    }

    public FeignExceptionTranslator(ThrowableMapper<HttpStatus, Class<? extends HttpResponseException>> throwableMapper) {
        Objects.requireNonNull(throwableMapper);
        this.throwableMapper = throwableMapper;
    }

    @Override
    protected Optional<HttpResponseException> execute(Throwable e) {
        if (e instanceof FeignException) {
            FeignException exception = (FeignException) e;
            log.error("{}: {}", e.getClass().getName(), e.getMessage());
            return throwableMapper.support(HttpStatus.resolve(exception.status())).map(super::createInstance);
        }
        return Optional.empty();
    }

    @Override
    public int getOrder() {
        return 40;
    }
}
