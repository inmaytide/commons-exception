package com.inmaytide.exception.web.mapper;

import com.inmaytide.exception.translator.ThrowableMapper;
import com.inmaytide.exception.web.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author inmaytide
 * @since 2020/11/26
 */
public class ResponseStatusExceptionMapper implements ThrowableMapper<HttpStatusCode, Class<? extends HttpResponseException>> {

    public static final ResponseStatusExceptionMapper DEFAULT_INSTANT = new ResponseStatusExceptionMapper();

    private final Map<HttpStatusCode, Class<? extends HttpResponseException>> relationships;

    private ResponseStatusExceptionMapper() {
        this.relationships = new ConcurrentHashMap<>();
        register(HttpStatus.BAD_REQUEST, BadRequestException.class);
        register(HttpStatus.UNAUTHORIZED, UnauthorizedException.class);
        register(HttpStatus.FORBIDDEN, AccessDeniedException.class);
        register(HttpStatus.NOT_FOUND, PathNotFoundException.class);
        register(HttpStatus.SERVICE_UNAVAILABLE, ServiceUnavailableException.class);
    }

    @Override
    public Optional<Class<? extends HttpResponseException>> map(HttpStatusCode key) {
        if (key == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(relationships.get(key));
    }

    @Override
    public void register(HttpStatusCode key, Class<? extends HttpResponseException> target) {
        if (relationships.containsKey(Objects.requireNonNull(key))) {
            throw new IllegalArgumentException(String.format("The mapping relationship already exists, %s", key));
        }
        relationships.put(key, Objects.requireNonNull(target));
    }

    @Override
    public void replace(HttpStatusCode key, Class<? extends HttpResponseException> target) {
        relationships.replace(Objects.requireNonNull(key), Objects.requireNonNull(target));
    }
}
