package com.inmaytide.exception.translator;

import java.util.Map;
import java.util.Optional;

/**
 * @author luomiao
 * @since 2020/11/26
 */
public interface ThrowableMapper<K, T extends Class<? extends Throwable>> {

    Optional<T> support(K key);

    void register(Map<K, T> mappers);

    void register(K key, T target);

    default void register(String key, T target) {
        throw new UnsupportedOperationException();
    }

    default void replace(K key, T target) {
        throw new UnsupportedOperationException();
    }

}
