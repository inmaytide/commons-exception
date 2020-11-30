package com.inmaytide.exception.starter.config;

import com.inmaytide.exception.http.HttpResponseException;
import com.inmaytide.exception.http.translator.HttpResponseExceptionTranslator;
import com.inmaytide.exception.http.translator.PredictableExceptionTranslator;
import com.inmaytide.exception.http.translator.ResponseStatusExceptionTranslator;
import com.inmaytide.exception.http.translator.UnknownExceptionTranslator;
import com.inmaytide.exception.translator.ThrowableTranslator;
import com.inmaytide.exception.translator.TranslatorChain;
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

    @Bean
    @ConditionalOnMissingBean(value = HttpResponseException.class, parameterizedContainer = ThrowableTranslator.class)
    public ThrowableTranslator<HttpResponseException> throwableTranslator() {
        TranslatorChain<HttpResponseException> chain = new TranslatorChain<>();
        chain.addTranslator(new HttpResponseExceptionTranslator());
        chain.addTranslator(new ResponseStatusExceptionTranslator());
        chain.addTranslator(new PredictableExceptionTranslator());
        chain.addTranslator(new UnknownExceptionTranslator());
        log.info("Default \"HttpResponseException\" translator is created.");
        return chain;
    }


}
