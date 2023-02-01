package com.inmaytide.exception.translator;

import org.springframework.core.Ordered;

import java.util.Optional;

/**
 * @author inmaytide
 * @since 2020/11/25
 */
public interface ThrowableTranslator<T extends Throwable> extends Ordered, Comparable<ThrowableTranslator<T>> {

    Optional<T> translate(Throwable throwable);

    @Override
    default int compareTo(ThrowableTranslator<T> o) {
        if (o == null) {
            return -1;
        }
        return Integer.compare(getOrder(), o.getOrder());
    }

}
