package com.inmaytide.exception.web.translator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmaytide.exception.translator.ThrowableMapper;
import com.inmaytide.exception.util.ApplicationContextHolder;
import com.inmaytide.exception.web.BadRequestException;
import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.domain.DefaultResponse;
import com.inmaytide.exception.web.mapper.ResponseStatusExceptionMapper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.Optional;

public class FeignExceptionTranslator extends AbstractHttpExceptionTranslator {

    private static final Logger log = LoggerFactory.getLogger(FeignExceptionTranslator.class);

    private final ThrowableMapper<HttpStatus, Class<? extends HttpResponseException>> throwableMapper;

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
            if (e instanceof FeignException.BadRequest) {
                FeignException.BadRequest exception = (FeignException.BadRequest) e;
                if (exception.responseBody().isPresent()) {
                    try {
                        DefaultResponse response = ApplicationContextHolder.getInstance().getBean(ObjectMapper.class).readerFor(DefaultResponse.class).readValue(exception.responseBody().get().array());
                        return Optional.of(new BadRequestException(response.getCode()));
                    } catch (Exception ignored) {

                    }
                }
            }
            FeignException exception = (FeignException) e;
            return throwableMapper.support(HttpStatus.resolve(exception.status())).map(super::createInstance);
        }
        return Optional.empty();
    }

    @Override
    public int getOrder() {
        return 40;
    }
}
