package com.inmaytide.exception.web.translator;

import com.inmaytide.exception.translator.ThrowableMapper;
import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.mapper.PredictableExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

/**
 * @author inmaytide
 * @since 2020/11/25
 */
public class PredictableExceptionTranslator extends AbstractHttpExceptionTranslator {

    public static final int ORDER = 30;

    private static final Logger log = LoggerFactory.getLogger(PredictableExceptionTranslator.class);

    private final ThrowableMapper<Class<? extends Throwable>, Class<? extends HttpResponseException>> throwableMapper;

    public PredictableExceptionTranslator() {
        throwableMapper = new PredictableExceptionMapper();
    }

    private PredictableExceptionTranslator(ThrowableMapper<Class<? extends Throwable>, Class<? extends HttpResponseException>> throwableMapper) {
        this.throwableMapper = Objects.requireNonNull(throwableMapper);
    }

    @Override
    public Optional<HttpResponseException> execute(Throwable e) {
        return throwableMapper.support(e.getClass()).map(cls -> createInstance(cls, e));
    }

    @Override
    protected Logger getLogger() {
        return log;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
