package com.inmaytide.exception.web.translator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmaytide.exception.translator.ThrowableMapper;
import com.inmaytide.exception.util.ApplicationContextHolder;
import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.domain.DefaultErrorCode;
import com.inmaytide.exception.web.domain.DefaultResponse;
import com.inmaytide.exception.web.mapper.ResponseStatusExceptionMapper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Objects;
import java.util.Optional;

/**
 * @author inmaytide
 * @since 2021/01/05
 */
public class FeignExceptionTranslator extends AbstractHttpExceptionTranslator {

    private static final Logger log = LoggerFactory.getLogger(FeignExceptionTranslator.class);

    private final ThrowableMapper<HttpStatusCode, Class<? extends HttpResponseException>> throwableMapper;

    public FeignExceptionTranslator() {
        throwableMapper = ResponseStatusExceptionMapper.getInstance();
    }

    public FeignExceptionTranslator(ThrowableMapper<HttpStatusCode, Class<? extends HttpResponseException>> throwableMapper) {
        this.throwableMapper = Objects.requireNonNull(throwableMapper);
    }

    @Override
    protected Optional<HttpResponseException> execute(Throwable e) {
        if (e instanceof FeignException exception) {
            if (exception.responseBody().isPresent()) {
                try {
                    DefaultResponse response = ApplicationContextHolder.getInstance().getBean(ObjectMapper.class).readerFor(DefaultResponse.class).readValue(exception.responseBody().get().array());
                    return Optional.of(new HttpResponseException(HttpStatus.resolve(exception.status()), new DefaultErrorCode(response.getCode(), response.getMessage()), e, response.getPlaceholders().toArray(new String[0])));
                } catch (Exception ignored) {

                }
            }
            return throwableMapper.support(HttpStatus.resolve(exception.status())).map(super::createInstance);
        }
        return Optional.empty();
    }

    @Override
    public int getOrder() {
        return 40;
    }
}
