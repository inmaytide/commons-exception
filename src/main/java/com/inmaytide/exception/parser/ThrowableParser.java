package com.inmaytide.exception.parser;

@FunctionalInterface
public interface ThrowableParser {

    Throwable parse(Throwable e);

}
