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
 * @author luomiao
 * @since 2020/11/27
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication
@Import({ServletExceptionHandlerConfiguration.class, ReactiveExceptionHandlerConfiguration.class})
public class WebExceptionHandlerAutoConfigure {

    private static final Logger log = LoggerFactory.getLogger(WebExceptionHandlerAutoConfigure.class);

    private static final String CLASS_NAME_FEIGN_EX = "feign.FeignException";

    @Bean
    @ConditionalOnMissingBean(value = HttpResponseException.class, parameterizedContainer = ThrowableTranslator.class)
    public ThrowableTranslator<HttpResponseException> throwableTranslator() {
        TranslatorDelegator<HttpResponseException> chain = new TranslatorDelegator<>();
        chain.addTranslator(new HttpResponseExceptionTranslator());
        chain.addTranslator(new ResponseStatusExceptionTranslator());
        chain.addTranslator(new PredictableExceptionTranslator());
        chain.addTranslator(new UnknownExceptionTranslator());
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
