package com.inmaytide.exception.config;

import com.inmaytide.exception.http.HttpResponseException;
import com.inmaytide.exception.http.translator.HttpResponseExceptionTranslator;
import com.inmaytide.exception.http.translator.PredictableExceptionTranslator;
import com.inmaytide.exception.http.translator.ResponseStatusExceptionTranslator;
import com.inmaytide.exception.http.translator.UnknownExceptionTranslator;
import com.inmaytide.exception.translator.ThrowableTranslator;
import com.inmaytide.exception.translator.TranslatorChain;
import org.springframework.context.annotation.Bean;

/**
 * @author luomiao
 * @since 2020/11/26
 */
public class HttpExceptionHandlerConfiguration {

    @Bean
    public ThrowableTranslator<HttpResponseException> translator() {
        TranslatorChain<HttpResponseException> chain = new TranslatorChain<>();
        chain.addTranslator(new HttpResponseExceptionTranslator());
        chain.addTranslator(new ResponseStatusExceptionTranslator());
        chain.addTranslator(new PredictableExceptionTranslator());
        chain.addTranslator(new UnknownExceptionTranslator());
        return chain;
    }

}
