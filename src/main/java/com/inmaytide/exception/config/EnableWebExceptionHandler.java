package com.inmaytide.exception.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({HttpExceptionHandlerConfiguration.class, WebExceptionHandlerConfiguration.class})
public @interface EnableWebExceptionHandler {
}
