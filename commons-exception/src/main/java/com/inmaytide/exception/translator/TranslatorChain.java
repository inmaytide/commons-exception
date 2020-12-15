package com.inmaytide.exception.translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author luomiao
 * @since 2020/11/26
 */
public class TranslatorChain<T extends Throwable> implements ThrowableTranslator<T> {

    private List<ThrowableTranslator<T>> translators;

    private boolean chainIsSorted;

    public TranslatorChain() {
        this.translators = new ArrayList<>();
        this.chainIsSorted = false;
    }

    public Optional<T> translate(Throwable throwable) {
        if (!chainIsSorted) {
            translators = translators.stream().sorted().collect(Collectors.toList());
            chainIsSorted = true;
        }
        return translators.stream().map(translator -> translator.translate(throwable))
                .filter(Optional::isPresent)
                .findFirst()
                .orElse(Optional.empty());
    }

    public void addTranslator(ThrowableTranslator<T> translator) {
        translators.add(translator);
        chainIsSorted = false;
    }

    @Override
    public int getOrder() {
        throw new UnsupportedOperationException();
    }

}
