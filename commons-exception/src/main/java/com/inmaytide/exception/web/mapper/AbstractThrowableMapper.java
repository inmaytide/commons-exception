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
        if (key == null) {
            return Optional.empty();
        }
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
        if (getContainer().containsKey(Objects.requireNonNull(key))) {
            throw new IllegalArgumentException(String.format("The mapping relationship already exists, %s", key));
        }
        getContainer().put(key, Objects.requireNonNull(target));
    }

    @Override
    public void register(String key, Class<? extends HttpResponseException> target) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replace(K key, Class<? extends HttpResponseException> target) {
        getContainer().replace(key, Objects.requireNonNull(target));
    }

    protected abstract Map<K, Class<? extends HttpResponseException>> getContainer();
}
