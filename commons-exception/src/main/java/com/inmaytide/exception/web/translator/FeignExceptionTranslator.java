package com.inmaytide.exception.web.translator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmaytide.exception.translator.ThrowableMapper;
import com.inmaytide.exception.util.ApplicationContextHolder;
import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.ServiceUnavailableException;
import com.inmaytide.exception.web.domain.DefaultErrorCode;
import com.inmaytide.exception.web.domain.DefaultResponse;
import com.inmaytide.exception.web.mapper.ResponseStatusExceptionMapper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Optional;

/**
 * @author inmaytide
 * @since 2021/01/05
 */
public class FeignExceptionTranslator extends AbstractHttpExceptionTranslator {

    private static final Logger LOG = LoggerFactory.getLogger(FeignExceptionTranslator.class);

    private final ThrowableMapper<HttpStatusCode, Class<? extends HttpResponseException>> throwableMapper;

    private ObjectMapper objectMapper;

    public FeignExceptionTranslator() {
        throwableMapper = ResponseStatusExceptionMapper.getInstance();
    }

    public FeignExceptionTranslator(ThrowableMapper<HttpStatusCode, Class<? extends HttpResponseException>> throwableMapper) {
        this.throwableMapper = Objects.requireNonNull(throwableMapper);
    }

    @Override
    protected Optional<HttpResponseException> execute(Throwable e) {
        if (e instanceof FeignException exception) {
            if (exception instanceof FeignException.ServiceUnavailable serviceUnavailable) {
                try {
                    return Optional.of(new ServiceUnavailableException(new URL(serviceUnavailable.request().url()).getHost()));
                } catch (MalformedURLException ex) {
                    LOG.error("Failed to get service name from the original exception, Cause by: ", e);
                }
            }
            return exception.responseBody()
                    .flatMap(this::transfer)
                    .map(res -> new HttpResponseException(HttpStatus.resolve(exception.status()), new DefaultErrorCode(res.getCode(), res.getMessage()), e, res.getPlaceholders() == null ? new String[0] : res.getPlaceholders().toArray(new String[0])))
                    .or(() -> throwableMapper.support(HttpStatus.resolve(exception.status())).map(super::createInstance));
        }
        return Optional.empty();
    }

    public Optional<DefaultResponse> transfer(ByteBuffer buffer) {
        try {
            return Optional.of(getObjectMapper().readValue(buffer.array(), DefaultResponse.class));
        } catch (Exception e) {
            LOG.error("Failed to convert error response from the original exception, Cause by: ", e);
        }
        return Optional.empty();
    }

    @Override
    public int getOrder() {
        return 40;
    }

    public ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = ApplicationContextHolder.getInstance().getBean(ObjectMapper.class);
        }
        return objectMapper;
    }
}
