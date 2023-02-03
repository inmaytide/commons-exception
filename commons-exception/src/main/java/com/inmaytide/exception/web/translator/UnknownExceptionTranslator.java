package com.inmaytide.exception.web.translator;

import com.inmaytide.exception.translator.ThrowableTranslator;
import com.inmaytide.exception.web.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * 通常放在翻译器链最后<br />
 * 将不可预知的异常转换为HttpResponseException, 并在日志中输出异常详细堆栈
 *
 * @author inmaytide
 * @since 2020/11/26
 */
public class UnknownExceptionTranslator implements ThrowableTranslator<HttpResponseException> {

    private static final Logger log = LoggerFactory.getLogger(UnknownExceptionTranslator.class);

    @Override
    public Optional<HttpResponseException> translate(Throwable e) {
        log.error("Unknown exception: ", e);
        return Optional.of(new HttpResponseException(e));
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
