package com.inmaytide.exception.web.servlet;

import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.domain.DefaultResponse;
import com.inmaytide.exception.translator.ThrowableTranslator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.OutputStream;

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
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        String path = request.getRequestURL().toString();
        log.error("Handing error: {}, {}, {} {}", e.getClass().getName(), e.getMessage(), request.getMethod(), path);
        if (log.isDebugEnabled()) {
            e.printStackTrace();
            log.error("Exception stack trace: ", e);
        }
        HttpResponseException exception = translator.translate(e).orElseGet(() -> new HttpResponseException(e));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(exception.getStatus().value());
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        try (OutputStream os = response.getOutputStream()) {
            os.write(DefaultResponse.withException(exception).URL(path).build().asBytes());
        } catch (IOException ioe) {
            log.error("Failed to write exception response content, Cause by: ", ioe);
        }
        return new ModelAndView();
    }

    @Override
    public int getOrder() {
        return -1000;
    }
}
