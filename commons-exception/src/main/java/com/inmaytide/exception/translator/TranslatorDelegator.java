package com.inmaytide.exception.translator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author inmaytide
 * @since 2020/11/26
 */
public class TranslatorDelegator<T extends Throwable> implements ThrowableTranslator<T> {

    private static final Logger LOG = LoggerFactory.getLogger(TranslatorDelegator.class);

    private List<ThrowableTranslator<T>> translators;

    private boolean innerSorted;

    public TranslatorDelegator() {
        this.translators = new ArrayList<>();
        this.innerSorted = false;
    }

    public Optional<T> translate(Throwable throwable) {
        if (!innerSorted) {
            translators = translators.stream().sorted().collect(Collectors.toList());
            innerSorted = true;
        }
        return translators.stream()
                .map(translator -> translator.translate(throwable))
                .filter(Optional::isPresent)
                .findFirst()
                .orElse(Optional.empty());
    }

    public void addTranslator(ThrowableTranslator<T> translator) {
        translators.add(translator);
        innerSorted = false;
    }

    @Override
    public int getOrder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Logger getLogger() {
        return LOG;
    }

}
