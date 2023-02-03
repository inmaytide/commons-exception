package com.inmaytide.exception.web.translator;

import com.inmaytide.exception.translator.ThrowableTranslator;
import com.inmaytide.exception.web.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * @author inmaytide
 * @since 2020/11/26
 */
public abstract class AbstractHttpExceptionTranslator implements ThrowableTranslator<HttpResponseException> {

    private static final Logger log = LoggerFactory.getLogger(AbstractHttpExceptionTranslator.class);

    @Override
    public Optional<HttpResponseException> translate(Throwable e) {
        Optional<HttpResponseException> translated = execute(e);
        if (translated.isPresent()) {
            return translated;
        }
        if (e.getCause() != null) {
            return translate(e.getCause());
        }
        return Optional.empty();
    }

    protected abstract Optional<HttpResponseException> execute(Throwable e);

    protected HttpResponseException createInstance(Class<? extends HttpResponseException> cls, Throwable cause) {
        try {
            Constructor<? extends HttpResponseException> constructor = cls.getConstructor(Throwable.class);
            return constructor.newInstance(cause);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            getLogger().warn("Failed to create HttpResponseException, Cause by: ", e);
            return new HttpResponseException(cause);
        }
    }


    protected HttpResponseException createInstance(Class<? extends HttpResponseException> cls) {
        try {
            Constructor<? extends HttpResponseException> constructor = cls.getConstructor();
            return constructor.newInstance();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            getLogger().warn("Failed to create HttpResponseException, Cause by: ", e);
            return new HttpResponseException();
        }
    }

    protected Logger getLogger() {
        return log;
    }

}
