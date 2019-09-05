package com.inmaytide.exception.http.handler.servlet;

import com.inmaytide.exception.parser.DefaultThrowableParser;
import com.inmaytide.exception.parser.ThrowableParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author luomiao
 * @since 2019/09/05
 */
public class DefaultExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    private final ThrowableParser parser;

    public DefaultExceptionHandler() {
        this(new DefaultThrowableParser());
    }

    public DefaultExceptionHandler(ThrowableParser parser) {
        this.parser = parser;
    }

    public void handle(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        log.error("Handing error: {}, {}, {}", e.getClass().getSimpleName(), e.getMessage(), request.getRequestURL().toString());
        new ThrowableTranslator(parser.parse(e), request.getRequestURL().toString()).write(response);
    }

}
