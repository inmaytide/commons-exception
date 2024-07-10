package com.inmaytide.exception.web.mapper;

import com.inmaytide.exception.translator.ThrowableMapper;
import com.inmaytide.exception.web.*;
import org.springframework.util.ClassUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author inmaytide
 * @since 2020/11/25
 */
public class PredictableExceptionMapper implements ThrowableMapper<Class<? extends Throwable>, Class<? extends HttpResponseException>> {

    public static final PredictableExceptionMapper DEFAULT_INSTANT = new PredictableExceptionMapper();

    private final Map<Class<? extends Throwable>, Class<? extends HttpResponseException>> relationships;

    public PredictableExceptionMapper() {
        this.relationships = new ConcurrentHashMap<>();
        register(IllegalArgumentException.class, BadRequestException.class);
        register("com.netflix.client.ClientException", ServiceUnavailableException.class, false);
        register("org.springframework.security.oauth2.common.exceptions.InvalidGrantException", BadCredentialsException.class, false);
        register("org.springframework.security.oauth2.common.exceptions.InvalidTokenException", BadCredentialsException.class, false);
        register("org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException", UnauthorizedException.class, false);
        register("org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException", BadCredentialsException.class, false);
        register("org.springframework.security.authentication.AuthenticationCredentialsNotFoundException", UnauthorizedException.class, false);
        register("org.springframework.security.access.AccessDeniedException", AccessDeniedException.class, false);
        register("org.springframework.security.authentication.InsufficientAuthenticationException", UnauthorizedException.class, false);
        register("org.springframework.web.servlet.NoHandlerFoundException", PathNotFoundException.class, false);
    }

    @Override
    public Optional<Class<? extends HttpResponseException>> map(Class<? extends Throwable> key) {
        if (key == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(relationships.get(key));
    }

    @Override
    public void register(Class<? extends Throwable> key, Class<? extends HttpResponseException> target) {
        if (relationships.containsKey(Objects.requireNonNull(key))) {
            throw new IllegalArgumentException(String.format("The mapping relationship already exists, %s", key));
        }
        relationships.put(key, Objects.requireNonNull(target));
    }

    @Override
    public void replace(Class<? extends Throwable> key, Class<? extends HttpResponseException> target) {
        relationships.replace(Objects.requireNonNull(key), Objects.requireNonNull(target));
    }

    @Override
    public void register(String key, Class<? extends HttpResponseException> target) {
        register(key, target, true);
    }

    private void register(String className, Class<? extends HttpResponseException> target, boolean classRequireFound) {
        try {
            Class<?> cls = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
            if (ClassUtils.isAssignable(Throwable.class, cls)) {
                register((Class<? extends Throwable>) cls, target);
            } else {
                throw new IllegalArgumentException(String.format("%s was not assignable from Throwable", className));
            }
        } catch (ClassNotFoundException e) {
            if (classRequireFound) {
                throw new IllegalArgumentException(e);
            }
        }
    }

}
