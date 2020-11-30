package com.inmaytide.exception.web.servlet;

import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.domain.DefaultResponse;
import com.inmaytide.exception.translator.ThrowableTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author luomiao
 * @since 2020/11/26
 */
public class DefaultHandlerExceptionResolver implements HandlerExceptionResolver, Ordered {

    private static final Logger log = LoggerFactory.getLogger(DefaultHandlerExceptionResolver.class);

    private final ThrowableTranslator<HttpResponseException> translator;

    public DefaultHandlerExceptionResolver(ThrowableTranslator<HttpResponseException> translator) {
        this.translator = translator;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception throwable) {
        String path = request.getRequestURL().toString();
        log.error("Handing error: {}, {}, {}", throwable.getClass().getName(), throwable.getMessage(), path);
        HttpResponseException exception = translator.translate(throwable).orElseGet(() -> new HttpResponseException(throwable));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(exception.getDefaultStatus().value());
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        try {
            response.getWriter().write(DefaultResponse.withException(exception).withUrl(path).build().toString());
        } catch (IOException e) {
            log.error("Failed to write exception response content, Cause by: ", e);
        }
        return new ModelAndView();
    }

    @Override
    public int getOrder() {
        return -1000;
    }
}