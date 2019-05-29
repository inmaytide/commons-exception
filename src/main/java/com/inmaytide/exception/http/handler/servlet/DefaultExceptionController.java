package com.inmaytide.exception.http.handler.servlet;

import com.inmaytide.exception.http.handler.domain.ResponseBody;
import com.inmaytide.exception.parser.DefaultThrowableParser;
import com.inmaytide.exception.parser.ThrowableParser;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;


public class DefaultExceptionController {

    private final ThrowableParser parser;

    public DefaultExceptionController() {
        this(new DefaultThrowableParser());
    }

    public DefaultExceptionController(ThrowableParser parser) {
        this.parser = parser;
    }

    @ExceptionHandler(Throwable.class)
    public Object exceptionHandler(HttpServletRequest request, Exception e){
        return ResponseBody.newBuilder().throwable(parser.parse(e)).url(request.getRequestURL().toString()).build();
    }

}
