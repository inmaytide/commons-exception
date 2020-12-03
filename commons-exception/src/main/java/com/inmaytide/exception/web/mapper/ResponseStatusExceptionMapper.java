package com.inmaytide.exception.web.mapper;

import com.inmaytide.exception.web.BadRequestException;
import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.PathNotFoundException;
import com.inmaytide.exception.web.ServiceUnavailableException;
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
