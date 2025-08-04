package com.inmaytide.exception.translator;

import java.util.Map;
import java.util.Optional;

/**
 * A generic interface for mapping business keys to specific exception types.
 *
 * @param <K> the type of the key used to map exceptions (e.g., error code, status identifier)
 * @param <T> the type of the exception (must be a subclass of {@link Throwable})
 * @author inmaytide
 * @since 2020/11/26
 */
public interface ThrowableMapper<K, T extends Class<? extends Throwable>> {

    /**
     * Retrieves the exception type associated with the given key.
     *
     * @param key the key to look up
     * @return an {@link Optional} containing the mapped exception type if found, or empty otherwise
     */
    Optional<T> map(K key);

    /**
     * Registers a new mapping between a key and an exception type.
     *
     * @param key    the key to register
     * @param target the exception class to associate with the key
     */
    void register(K key, T target);

    /**
     * Replaces an existing mapping for a key with a new exception type.
     *
     * @param key    the key to update
     * @param target the new exception class to associate with the key
     */
    void replace(K key, T target);

    /**
     * Registers a batch of key-to-exception mappings.
     *
     * @param mappers a map of keys to exception types
     * @throws IllegalArgumentException if the map is null or empty
     */
    default void register(Map<K, T> mappers) {
        if (mappers == null || mappers.isEmpty()) {
            throw new IllegalArgumentException("The mapping relationships to be registered cannot be null or empty.");
        }
        mappers.forEach(this::register);
    }

}
