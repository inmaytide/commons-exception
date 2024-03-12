package com.inmaytide.exception.translator;

import java.util.Map;
import java.util.Optional;

/**
 * @author inmaytide
 * @since 2020/11/26
 */
public interface ThrowableMapper<K, T extends Class<? extends Throwable>> {

    Optional<T> map(K key);

    void register(K key, T target);

    void replace(K key, T target);

    default void register(String key, T target) {
        throw new UnsupportedOperationException();
    }

    default void register(Map<K, T> mappers) {
        if (mappers == null || mappers.isEmpty()) {
            throw new IllegalArgumentException("The mapping relationship to be registered cannot be empty");
        }
        mappers.forEach(this::register);
    }

}
