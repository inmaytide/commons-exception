package com.inmaytide.exception.starter.config;

import com.inmaytide.exception.http.HttpResponseException;
import com.inmaytide.exception.http.reactive.DefaultExceptionHandler;
import com.inmaytide.exception.translator.ThrowableTranslator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebExceptionHandler;

/**
 * @author luomiao
 * @since 2020/11/26
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(WebExceptionHandler.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveExceptionHandlerConfiguration {

    @Bean
    // @ConditionalOnMissingBean(WebExceptionHandler.class)
    public WebExceptionHandler exceptionHandler(ThrowableTranslator<HttpResponseException> translator) {
        return new DefaultExceptionHandler(translator);
    }

}


