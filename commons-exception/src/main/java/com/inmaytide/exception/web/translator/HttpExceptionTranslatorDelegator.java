package com.inmaytide.exception.web.translator;

import com.inmaytide.exception.translator.ThrowableTranslator;
import com.inmaytide.exception.web.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author inmaytide
 * @since 2024/3/11
 */
public class HttpExceptionTranslatorDelegator {

    private static final Logger LOG = LoggerFactory.getLogger(HttpExceptionTranslatorDelegator.class);

    private final Set<ThrowableTranslator<HttpResponseException>> translators;

    public HttpExceptionTranslatorDelegator(List<ThrowableTranslator<HttpResponseException>> translators) {
        this.translators = new TreeSet<>(translators);
    }

    public Optional<HttpResponseException> translate(Throwable throwable) {
        return translators.stream()
                .map(translator -> translator.translate(throwable))
                .filter(Optional::isPresent)
                .findFirst()
                .orElse(Optional.empty());
    }

    public void addTranslator(ThrowableTranslator<HttpResponseException> translator) {
        translators.add(translator);
    }
}
