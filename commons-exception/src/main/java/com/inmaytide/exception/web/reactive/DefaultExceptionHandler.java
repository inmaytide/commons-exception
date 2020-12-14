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
        String path = exchange.getRequest().getPath().pathWithinApplication().value();
        log.error("Handing error: {}, {}, {}", throwable.getClass().getName(), throwable.getMessage(), exchange.getRequest().getMethodValue() + " " + path);
        if (log.isDebugEnabled()) {
            log.error("", throwable);
        }
        HttpResponseException exception = translator.translate(throwable).orElseGet(() -> new HttpResponseException(throwable));
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(exception.getStatus());
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return Mono.just(DefaultResponse.withException(exception).withUrl(path).build().asDataBuffer(response.bufferFactory()))
                .map(Mono::just)
                .map(Mono::just)
                .flatMap(response::writeAndFlushWith);
    }

    public Mono<ServerResponse> pathNotFound(ServerRequest request) {
        String path = request.path();
        DefaultResponse body = DefaultResponse.withException(new PathNotFoundException()).withUrl(path).build();
        return ServerResponse
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(body));
    }

    @Override
    public int getOrder() {
        return -1000;
    }
}
