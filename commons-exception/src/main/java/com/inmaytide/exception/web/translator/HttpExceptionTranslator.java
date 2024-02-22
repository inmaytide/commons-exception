package com.inmaytide.exception.web.translator;

import com.inmaytide.exception.translator.ThrowableTranslator;
import com.inmaytide.exception.web.HttpResponseException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * @author inmaytide
 * @since 2020/11/26
 */
public interface HttpExceptionTranslator extends ThrowableTranslator<HttpResponseException> {

    @Override
    default Optional<HttpResponseException> translate(Throwable e) {
        Optional<HttpResponseException> translated = execute(e);
        if (translated.isPresent()) {
            return translated;
        }
        if (e.getCause() != null) {
            return translate(e.getCause());
        }
        return Optional.empty();
    }

    Optional<HttpResponseException> execute(Throwable e);

    default HttpResponseException createExceptionInstance(Class<? extends HttpResponseException> cls) {
        try {
            Constructor<? extends HttpResponseException> constructor = cls.getConstructor();
            return constructor.newInstance();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            getLogger().warn("Failed to create HttpResponseException, Cause by: ", e);
            return new HttpResponseException();
        }
    }

    default HttpResponseException createExceptionInstance(Class<? extends HttpResponseException> cls, Throwable cause) {
        try {
            Constructor<? extends HttpResponseException> constructor = cls.getConstructor(Throwable.class);
            return constructor.newInstance(cause);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            getLogger().warn("Failed to create HttpResponseException, Cause by: ", e);
            return new HttpResponseException(cause);
        }
    }

}
