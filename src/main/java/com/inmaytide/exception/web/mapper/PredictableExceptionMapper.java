package com.inmaytide.exception.web.mapper;

import com.inmaytide.exception.web.BadCredentialsException;
import com.inmaytide.exception.web.BadRequestException;
import com.inmaytide.exception.web.HttpResponseException;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luomiao
 * @since 2020/11/25
 */
public class PredictableExceptionMapper extends AbstractThrowableMapper<Class<? extends Throwable>> {

    private static final Map<Class<? extends Throwable>, Class<? extends HttpResponseException>> CONTAINER = new ConcurrentHashMap<>();

    @Override
    public void register(String key, Class<? extends HttpResponseException> target) {
        register(key, target, true);
    }

    private void register(String className, Class<? extends HttpResponseException> target, boolean classRequireFound) {
        try {
            Class<?> cls = Class.forName(className);
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
    protected Map<Class<? extends Throwable>, Class<? extends HttpResponseException>> getContainer() {
        return CONTAINER;
    }

    public PredictableExceptionMapper() {
        register(IllegalArgumentException.class, BadRequestException.class);
        register("org.springframework.security.oauth2.common.exceptions.InvalidGrantException", BadCredentialsException.class, false);
    }
}
