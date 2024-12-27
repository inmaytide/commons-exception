package com.inmaytide.exception.web.translator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmaytide.exception.translator.ThrowableMapper;
import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.ServiceUnavailableException;
import com.inmaytide.exception.web.domain.DefaultErrorCode;
import com.inmaytide.exception.web.domain.DefaultResponse;
import com.inmaytide.exception.web.mapper.ResponseStatusExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

/**
 * @author inmaytide
 * @since 2024/12/27
 */
public class WebClientResponseExceptionTranslator implements HttpExceptionTranslator {

    private static final Logger LOG = LoggerFactory.getLogger(WebClientResponseExceptionTranslator.class);

    private final ThrowableMapper<HttpStatusCode, Class<? extends HttpResponseException>> throwableMapper;

    private final ObjectMapper objectMapper;

    public WebClientResponseExceptionTranslator() {
        this(new ObjectMapper());
    }

    public WebClientResponseExceptionTranslator(ObjectMapper objectMapper) {
        this(objectMapper, ResponseStatusExceptionMapper.DEFAULT_INSTANT);
    }

    public WebClientResponseExceptionTranslator(ObjectMapper objectMapper, ResponseStatusExceptionMapper throwableMapper) {
        this.objectMapper = objectMapper;
        this.throwableMapper = throwableMapper;
    }

    @Override
    public Optional<HttpResponseException> execute(Throwable e) {
        if (e instanceof WebClientResponseException exception) {
            if (exception instanceof WebClientResponseException.ServiceUnavailable serviceUnavailable) {
                String serviceName = "";
                if (serviceUnavailable.getRequest() != null) {
                    serviceName = serviceUnavailable.getRequest().getURI().getHost();
                }
                return Optional.of(new ServiceUnavailableException(serviceName));
            }
            return Optional.of(exception.getResponseBodyAsByteArray())
                    .flatMap(this::transfer)
                    .map(res -> new HttpResponseException(HttpStatus.resolve(exception.getStatusCode().value()), new DefaultErrorCode(res.getCode(), res.getMessage()), e, res.getPlaceholders() == null ? new String[0] : res.getPlaceholders().toArray(new String[0])))
                    .or(() -> throwableMapper.map(HttpStatus.resolve(exception.getStatusCode().value())).map(this::createExceptionInstance));
        }
        return Optional.empty();
    }

    public Optional<DefaultResponse> transfer(byte[] buffer) {
        try {
            return Optional.of(objectMapper.readValue(buffer, DefaultResponse.class));
        } catch (Exception e) {
            LOG.error("Failed to convert error response from the original exception, Cause by: ", e);
        }
        return Optional.empty();
    }

    @Override
    public Logger getLogger() {
        return LOG;
    }

    @Override
    public int getOrder() {
        return 45;
    }
}
