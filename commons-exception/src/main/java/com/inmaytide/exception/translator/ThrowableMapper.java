package com.inmaytide.exception.translator;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author inmaytide
 * @since 2020/11/26
 */
public interface ThrowableMapper<K, T extends Class<? extends Throwable>> {

    default Optional<T> map(K key) {
        if (key == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(getContainer().get(key));
    }

    default void register(Map<K, T> mappers) {
        if (mappers == null || mappers.isEmpty()) {
            throw new IllegalArgumentException("The mapping relationship to be registered cannot be empty");
        }
        mappers.forEach(this::register);
    }

    default void register(K key, T target) {
        if (getContainer().containsKey(Objects.requireNonNull(key))) {
            throw new IllegalArgumentException(String.format("The mapping relationship already exists, %s", key));
        }
        getContainer().put(key, Objects.requireNonNull(target));
    }

    default void register(String key, T target) {
        throw new UnsupportedOperationException();
    }

    default void replace(K key, T target) {
        getContainer().replace(key, Objects.requireNonNull(target));
    }

    Map<K, T> getContainer();

}
