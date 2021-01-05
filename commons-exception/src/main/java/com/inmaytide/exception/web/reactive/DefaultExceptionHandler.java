package com.inmaytide.exception.web.reactive;

import com.inmaytide.exception.translator.ThrowableTranslator;
import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.PathNotFoundException;
import com.inmaytide.exception.web.domain.DefaultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;


/**
 * @author luomiao
 * @since 2020/11/25
 */
public class DefaultExceptionHandler implements WebExceptionHandler, Ordered {

    private static final Logger log = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    public final ThrowableTranslator<HttpResponseException> translator;

    public DefaultExceptionHandler(ThrowableTranslator<HttpResponseException> translator) {
        this.translator = translator;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        DefaultResponse body = resolve(exchange.getRequest(), throwable);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(body.getStatus());
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return Mono.just(body.asDataBuffer(response.bufferFactory()))
                .map(Mono::just)
                .map(Mono::just)
                .flatMap(response::writeAndFlushWith);
    }

    public Mono<ServerResponse> pathNotFound(ServerRequest request) {
        return ServerResponse
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(DefaultResponse.withException(new PathNotFoundException()).withUrl(request.path()).build()));
    }

    private DefaultResponse resolve(ServerHttpRequest request, Throwable e) {
        log.error("Handing error: {}, {}, {} {}", e.getClass().getName(), e.getMessage(), request.getMethodValue(), getPath(request));
        if (log.isDebugEnabled()) {
            e.printStackTrace();
            log.error("Exception stack trace: ", e);
        }
        HttpResponseException exception = translator.translate(e).orElseGet(() -> new HttpResponseException(e));
        return DefaultResponse.withException(exception).withUrl(getPath(request)).build();
    }

    private String getPath(ServerHttpRequest request) {
        return request.getPath().pathWithinApplication().value();
    }

    @Override
    public int getOrder() {
        return -1000;
    }
}
