package com.inmaytide.exception.http.handler.reactive;

import com.inmaytide.exception.http.domain.ResponseBody;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.status;

class ThrowableTranslator {

    private final ResponseBody body;

    private ThrowableTranslator(Throwable throwable, String url) {
        this.body = ResponseBody.newBuilder().throwable(throwable).url(url).build();
    }

    static <T extends Throwable> Mono<ThrowableTranslator> translate(final Mono<T> throwable, String path) {
        return throwable.flatMap(error -> Mono.just(new ThrowableTranslator(error, path)));
    }

    Mono<Void> write(ServerHttpResponse response) {
        response.setStatusCode(body.getStatus());
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        DataBuffer buffer = response.bufferFactory().wrap(body.asBytes());
        Mono<DataBuffer> mono = Mono.just(buffer);
        return response.writeAndFlushWith(Mono.just(mono));
    }

    Mono<ServerResponse> getResponse() {
        return status(body.getStatus())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(body), ResponseBody.class);
    }

}
