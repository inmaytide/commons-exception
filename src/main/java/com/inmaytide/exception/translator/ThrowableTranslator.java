package com.inmaytide.exception.translator;

import org.springframework.core.Ordered;

import java.util.Optional;

/**
 * @author luomiao
 * @since 2020/11/25
 */
public interface ThrowableTranslator<T extends Throwable> extends Ordered {

    Optional<T> translate(Throwable throwable);

}
