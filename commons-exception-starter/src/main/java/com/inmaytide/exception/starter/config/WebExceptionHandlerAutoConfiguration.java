package com.inmaytide.exception.starter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmaytide.exception.translator.ThrowableTranslator;
import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.translator.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author inmaytide
 * @since 2020/11/27
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication
public class WebExceptionHandlerAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(WebExceptionHandlerAutoConfiguration.class);

    @Bean
    public HttpResponseExceptionTranslator httpResponseExceptionTranslator() {
        return new HttpResponseExceptionTranslator();
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.web.bind.MethodArgumentNotValidException")
    public MethodArgumentNotValidExceptionTranslator methodArgumentNotValidExceptionTranslator() {
        return new MethodArgumentNotValidExceptionTranslator();
    }

    @Bean
    @ConditionalOnMissingBean(ResponseStatusExceptionTranslator.class)
    public ResponseStatusExceptionTranslator responseStatusExceptionTranslator() {
        return new ResponseStatusExceptionTranslator();
    }

    @Bean
    @ConditionalOnMissingBean(PredictableExceptionTranslator.class)
    public PredictableExceptionTranslator predictableExceptionTranslator() {
        return new PredictableExceptionTranslator();
    }

    @Bean
    @ConditionalOnClass(name = "feign.FeignException")
    public FeignExceptionTranslator feignExceptionTranslator(ObjectMapper objectMapper) {
        return new FeignExceptionTranslator(objectMapper);
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.web.reactive.function.client.WebClientResponseException")
    public WebClientResponseExceptionTranslator webClientResponseExceptionTranslator(ObjectMapper objectMapper) {
        return new WebClientResponseExceptionTranslator(objectMapper);
    }

    @Bean
    public UnknownExceptionTranslator unknownExceptionTranslator() {
        return new UnknownExceptionTranslator();
    }


    @Bean
    @ConditionalOnMissingBean(value = HttpExceptionTranslatorDelegator.class)
    public HttpExceptionTranslatorDelegator httpExceptionTranslatorDelegator(List<ThrowableTranslator<HttpResponseException>> translators) {
        return new HttpExceptionTranslatorDelegator(translators);
    }


}
