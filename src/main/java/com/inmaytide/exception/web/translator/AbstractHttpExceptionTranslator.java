package com.inmaytide.exception.web.translator;

import com.inmaytide.exception.translator.ThrowableTranslator;
import com.inmaytide.exception.web.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author luomiao
 * @since 2020/11/26
 */
public abstract class AbstractHttpExceptionTranslator implements ThrowableTranslator<HttpResponseException> {

    private static final Logger log = LoggerFactory.getLogger(AbstractHttpExceptionTranslator.class);

    protected HttpResponseException createInstance(Class<? extends HttpResponseException> cls, Throwable cause) {
        try {
            Constructor<? extends HttpResponseException> constructor = cls.getConstructor(Throwable.class);
            return constructor.newInstance(cause);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            getLogger().warn("Failed to create HttpResponseException, Cause by: ", e);
            return new HttpResponseException(cause);
        }
    }

    protected Logger getLogger() {
        return log;
    }

}
