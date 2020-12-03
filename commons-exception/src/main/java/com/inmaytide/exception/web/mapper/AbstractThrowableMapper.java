package com.inmaytide.exception.web.mapper;

import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.translator.ThrowableMapper;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author luomiao
 * @since 2020/11/26
 */
public abstract class AbstractThrowableMapper<K> implements ThrowableMapper<K, Class<? extends HttpResponseException>> {

    @Override
    public Optional<Class<? extends HttpResponseException>> support(K key) {
        return Optional.ofNullable(getContainer().get(key));
    }

    @Override
    public void register(Map<K, Class<? extends HttpResponseException>> mappers) {
        if (mappers == null || mappers.isEmpty()) {
            throw new IllegalArgumentException("The mapping relationship to be registered cannot be empty");
        }
        mappers.forEach(this::register);
    }

    @Override
    public void register(K key, Class<? extends HttpResponseException> target) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(target);
        if (getContainer().containsKey(key)) {
            throw new IllegalArgumentException(String.format("The mapping relationship already exists, %s", key.toString()));
        }
        getContainer().put(key, target);
    }

    protected abstract Map<K, Class<? extends HttpResponseException>> getContainer();
}
