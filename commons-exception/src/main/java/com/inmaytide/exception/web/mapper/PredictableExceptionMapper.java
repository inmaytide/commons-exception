package com.inmaytide.exception.web.mapper;

import com.inmaytide.exception.translator.ThrowableMapper;
import com.inmaytide.exception.web.*;
import org.springframework.util.ClassUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author inmaytide
 * @since 2020/11/25
 */
public class PredictableExceptionMapper implements ThrowableMapper<Class<? extends Throwable>, Class<? extends HttpResponseException>> {

    private static final Map<Class<? extends Throwable>, Class<? extends HttpResponseException>> CONTAINER = new ConcurrentHashMap<>();

    public static final PredictableExceptionMapper DEFAULT_INSTANT = new PredictableExceptionMapper();

    public PredictableExceptionMapper() {
        register(IllegalArgumentException.class, BadRequestException.class);
        register("com.netflix.client.ClientException", ServiceUnavailableException.class, false);
        register("org.springframework.security.oauth2.common.exceptions.InvalidGrantException", BadCredentialsException.class, false);
        register("org.springframework.security.oauth2.common.exceptions.InvalidTokenException", BadCredentialsException.class, false);
        register("org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException", UnauthorizedException.class, false);
        register("org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException", BadCredentialsException.class, false);
        register("org.springframework.security.authentication.AuthenticationCredentialsNotFoundException", UnauthorizedException.class, false);
        register("org.springframework.security.access.AccessDeniedException", AccessDeniedException.class, false);
        register("org.springframework.security.authentication.InsufficientAuthenticationException", UnauthorizedException.class, false);
    }

    @Override
    public void register(String key, Class<? extends HttpResponseException> target) {
        register(key, target, true);
    }

    private void register(String className, Class<? extends HttpResponseException> target, boolean classRequireFound) {
        try {
            Class<?> cls = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
            if (isAssignableFromThrowable(cls)) {
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

    private boolean isAssignableFromThrowable(Class<?> cls) {
        if (cls == null) {
            return false;
        }
        Class<?> superclass = cls.getSuperclass();
        if (Objects.equals(superclass, Throwable.class)) {
            return true;
        }
        return isAssignableFromThrowable(superclass);
    }

    @Override
    public Map<Class<? extends Throwable>, Class<? extends HttpResponseException>> getContainer() {
        return CONTAINER;
    }
}
