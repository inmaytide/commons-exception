package com.inmaytide.exception.web.reactive;

import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.PathNotFoundException;
import com.inmaytide.exception.web.domain.DefaultResponse;
import com.inmaytide.exception.web.translator.HttpExceptionTranslatorDelegator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;


/**
 * @author inmaytide
 * @since 2020/11/25
 */
public record DefaultExceptionHandler(HttpExceptionTranslatorDelegator translator) implements WebExceptionHandler, Ordered {

    private static final Logger log = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        DefaultResponse body = resolve(exchange.getRequest(), throwable);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.resolve(body.getStatus()));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return Mono.just(body.asDataBuffer(response.bufferFactory()))
                .map(Mono::just)
                .map(Mono::just)
                .flatMap(response::writeAndFlushWith);
    }

    public Mono<ServerResponse> pathNotFound(@NonNull ServerRequest request) {
        return ServerResponse
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(DefaultResponse.withException(new PathNotFoundException()).URL(request.path()).build()));
    }

    private DefaultResponse resolve(ServerHttpRequest request, Throwable e) {
        log.error("Handing error: {}, {}, {} {}", e.getClass().getName(), e.getMessage(), request.getMethod().name(), getPath(request));
        if (log.isDebugEnabled()) {
            log.error("Exception stack trace: ", e);
        }
        HttpResponseException exception = translator.translate(e).orElseGet(() -> new HttpResponseException(e));
        return DefaultResponse.withException(exception).URL(getPath(request)).build();
    }

    private String getPath(ServerHttpRequest request) {
        return request.getPath().pathWithinApplication().value();
    }

    @Override
    public int getOrder() {
        return -1000;
    }
}
