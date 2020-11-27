package com.inmaytide.exception.config;

import com.inmaytide.exception.http.HttpResponseException;
import com.inmaytide.exception.http.mvc.DefaultHandlerExceptionResolver;
import com.inmaytide.exception.translator.ThrowableTranslator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author luomiao
 * @since 2020/11/26
 */
public class WebExceptionHandlerConfiguration {

    @Bean
    @ConditionalOnMissingBean(DefaultHandlerExceptionResolver.class)
    public DefaultHandlerExceptionResolver exceptionHandler(ThrowableTranslator<HttpResponseException> translator) {
        return new DefaultHandlerExceptionResolver(translator);
    }
}
