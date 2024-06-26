package com.inmaytide.exception.starter.config;

import com.inmaytide.exception.web.reactive.DefaultExceptionHandler;
import com.inmaytide.exception.web.translator.HttpExceptionTranslatorDelegator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebExceptionHandler;

/**
 * @author inmaytide
 * @since 2020/11/26
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(WebExceptionHandler.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveExceptionHandlerConfiguration {

    @Bean
    public WebExceptionHandler exceptionHandler(HttpExceptionTranslatorDelegator translator) {
        return new DefaultExceptionHandler(translator);
    }

}


