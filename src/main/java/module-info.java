module commons.exception {
    requires reactor.core;
    requires org.reactivestreams;
    requires spring.web;
    requires spring.core;
    requires spring.webflux;
    requires spring.context;
    requires slf4j.api;
    requires javax.servlet.api;

    exports com.inmaytide.exception.http;
    exports com.inmaytide.exception.http.handler.reactive;
    exports com.inmaytide.exception.parser;
}