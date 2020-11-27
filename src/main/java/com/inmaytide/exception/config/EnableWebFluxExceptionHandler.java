package com.inmaytide.exception.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({HttpExceptionHandlerConfiguration.class, WebFluxExceptionHandlerConfiguration.class})
public @interface EnableWebFluxExceptionHandler {


}
