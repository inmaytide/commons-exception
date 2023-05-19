package com.inmaytide.exception.starter.config;

import com.inmaytide.exception.translator.ThrowableTranslator;
import com.inmaytide.exception.translator.TranslatorDelegator;
import com.inmaytide.exception.util.ApplicationContextHolder;
import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.translator.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author inmaytide
 * @since 2020/11/27
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication
public class WebExceptionHandlerAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(WebExceptionHandlerAutoConfiguration.class);

    private static final String CLASS_NAME_FEIGN_EX = "feign.FeignException";

    @Bean
    @ConditionalOnMissingBean(value = HttpResponseException.class, parameterizedContainer = ThrowableTranslator.class)
    public ThrowableTranslator<HttpResponseException> throwableTranslator() {
        TranslatorDelegator<HttpResponseException> chain = new TranslatorDelegator<>();
        chain.addTranslator(new HttpResponseExceptionTranslator());
        chain.addTranslator(new ResponseStatusExceptionTranslator());
        chain.addTranslator(new PredictableExceptionTranslator());
        chain.addTranslator(new UnknownExceptionTranslator());
        chain.addTranslator(new MethodArgumentNotValidExceptionTranslator());
        try {
            Class.forName(CLASS_NAME_FEIGN_EX);
            chain.addTranslator(new FeignExceptionTranslator());
        } catch (ClassNotFoundException e) {
            log.debug("\"{}\" is not found, \"FeignExceptionTranslator\" is not initialized", CLASS_NAME_FEIGN_EX);
        }
        log.info("Default \"HttpResponseException\" translator is created.");
        return chain;
    }

    @Bean(name = "appContextHolder")
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }


}
