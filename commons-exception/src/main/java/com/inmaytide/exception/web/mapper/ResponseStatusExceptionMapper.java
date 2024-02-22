package com.inmaytide.exception.web.mapper;

import com.inmaytide.exception.translator.ThrowableMapper;
import com.inmaytide.exception.web.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author inmaytide
 * @since 2020/11/26
 */
public class ResponseStatusExceptionMapper implements ThrowableMapper<HttpStatusCode, Class<? extends HttpResponseException>> {

    private static final Map<HttpStatusCode, Class<? extends HttpResponseException>> CONTAINER = new ConcurrentHashMap<>();

    public static final ResponseStatusExceptionMapper DEFAULT_INSTANT = new ResponseStatusExceptionMapper();

    private ResponseStatusExceptionMapper() {
        register(HttpStatus.BAD_REQUEST, BadRequestException.class);
        register(HttpStatus.UNAUTHORIZED, UnauthorizedException.class);
        register(HttpStatus.FORBIDDEN, AccessDeniedException.class);
        register(HttpStatus.NOT_FOUND, PathNotFoundException.class);
        register(HttpStatus.SERVICE_UNAVAILABLE, ServiceUnavailableException.class);
    }

    @Override
    public Map<HttpStatusCode, Class<? extends HttpResponseException>> getContainer() {
        return CONTAINER;
    }


}
