package com.inmaytide.exception.starter.config;

import com.inmaytide.exception.http.HttpResponseException;
import com.inmaytide.exception.http.mvc.DefaultHandlerExceptionResolver;
import com.inmaytide.exception.translator.ThrowableTranslator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * @author luomiao
 * @since 2020/11/26
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(HandlerExceptionResolver.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletExceptionHandlerConfiguration {

    @Bean
    // @ConditionalOnMissingBean(HandlerExceptionResolver.class)
    public HandlerExceptionResolver exceptionResolver(ThrowableTranslator<HttpResponseException> translator) {
        return new DefaultHandlerExceptionResolver(translator);
    }
}
