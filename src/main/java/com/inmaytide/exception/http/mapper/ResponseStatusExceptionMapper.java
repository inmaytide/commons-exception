package com.inmaytide.exception.http.mapper;

import com.inmaytide.exception.http.BadRequestException;
import com.inmaytide.exception.http.HttpResponseException;
import com.inmaytide.exception.http.PathNotFoundException;
import com.inmaytide.exception.http.ServiceUnavailableException;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luomiao
 * @since 2020/11/26
 */
public class ResponseStatusExceptionMapper extends AbstractThrowableMapper<HttpStatus> {

    private static final Map<HttpStatus, Class<? extends HttpResponseException>> CONTAINER = new ConcurrentHashMap<>();

    @Override
    protected Map<HttpStatus, Class<? extends HttpResponseException>> getContainer() {
        return CONTAINER;
    }

    public ResponseStatusExceptionMapper() {
        register(HttpStatus.NOT_FOUND, PathNotFoundException.class);
        register(HttpStatus.SERVICE_UNAVAILABLE, ServiceUnavailableException.class);
        register(HttpStatus.BAD_REQUEST, BadRequestException.class);
    }

}
