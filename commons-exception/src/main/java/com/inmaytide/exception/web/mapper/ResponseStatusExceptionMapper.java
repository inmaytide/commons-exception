package com.inmaytide.exception.web.mapper;

import com.inmaytide.exception.web.*;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luomiao
 * @since 2020/11/26
 */
public class ResponseStatusExceptionMapper extends AbstractThrowableMapper<HttpStatus> {

    private static final Map<HttpStatus, Class<? extends HttpResponseException>> CONTAINER = new ConcurrentHashMap<>();

    private static final ResponseStatusExceptionMapper INSTANT = new ResponseStatusExceptionMapper();

    @Override
    protected Map<HttpStatus, Class<? extends HttpResponseException>> getContainer() {
        return CONTAINER;
    }

    private ResponseStatusExceptionMapper() {
        register(HttpStatus.NOT_FOUND, PathNotFoundException.class);
        register(HttpStatus.UNAUTHORIZED, UnauthorizedException.class);
        register(HttpStatus.FORBIDDEN, AccessDeniedException.class);
        register(HttpStatus.SERVICE_UNAVAILABLE, ServiceUnavailableException.class);
        register(HttpStatus.BAD_REQUEST, BadRequestException.class);
    }

    public static ResponseStatusExceptionMapper getInstance() {
        return INSTANT;
    }

}
