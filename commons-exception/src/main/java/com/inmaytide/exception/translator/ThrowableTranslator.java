package com.inmaytide.exception.translator;

import org.slf4j.Logger;
import org.springframework.core.Ordered;

import java.util.Optional;

/**
 * A contract for translating general {@link Throwable} instances into specific exception types.
 *
 * <p>Implementations can be prioritized using {@link Ordered}, and compared by order to determine processing sequence.</p>
 *
 * @param <T> the target exception type this translator can produce
 * @author inmaytide
 * @since 2020/11/25
 */
public interface ThrowableTranslator<T extends Throwable> extends Ordered, Comparable<ThrowableTranslator<T>> {

    /**
     * Attempts to translate the given {@link Throwable} into a specific exception type.
     *
     * @param throwable the original throwable
     * @return an {@link Optional} containing the translated exception, or empty if no match found
     */
    Optional<T> translate(Throwable throwable);

    /**
     * Returns the logger associated with this translator for logging purposes.
     *
     * @return the logger
     */
    Logger getLogger();

    /**
     * Compares this translator with another based on their {@link #getOrder()} values.
     *
     * @param o the other translator
     * @return a negative integer, zero, or a positive integer as this translator is less than,
     * equal to, or greater than the specified translator
     */
    @Override
    default int compareTo(ThrowableTranslator<T> o) {
        if (o == null) {
            return -1;
        }
        return Integer.compare(getOrder(), o.getOrder());
    }

}
