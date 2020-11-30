package com.inmaytide.exception.translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author luomiao
 * @since 2020/11/26
 */
public class TranslatorChain<T extends Throwable> implements ThrowableTranslator<T> {

    private final List<ThrowableTranslator<T>> translators;

    public TranslatorChain() {
        this.translators = new ArrayList<>();
    }

    public Optional<T> translate(Throwable throwable) {
        throwable = getCause(throwable);
        for (ThrowableTranslator<T> translator : translators) {
            Optional<T> op = translator.translate(throwable);
            if (op.isPresent()) {
                return op;
            }
        }
        return Optional.empty();
    }

    public void addTranslator(ThrowableTranslator<T> translator) {
        translators.add(translator);
    }

    protected Throwable getCause(Throwable e) {
        if (e.getCause() == null) {
            return e;
        }
        return getCause(e.getCause());
    }

    @Override
    public int getOrder() {
        throw new UnsupportedOperationException();
    }
}
