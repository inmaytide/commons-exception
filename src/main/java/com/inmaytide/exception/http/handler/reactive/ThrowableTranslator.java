package com.inmaytide.exception.http.handler.reactive;

import com.inmaytide.exception.http.handler.domain.ResponseBody;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.status;

public class ThrowableTranslator {

    private final ResponseBody throwableResponseBody;

    private ThrowableTranslator(Throwable throwable, String url) {
        this.throwableResponseBody = ResponseBody.newBuilder().throwable(throwable).url(url).build();
    }

    public static <T extends Throwable> Mono<ThrowableTranslator> translate(final Mono<T> throwable, String path) {
        return throwable.flatMap(error -> Mono.just(new ThrowableTranslator(error, path)));
    }

    public Mono<Void> write(ServerHttpResponse response) {
        response.setStatusCode(getHttpStatus());
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        DataBuffer buffer = response.bufferFactory().wrap(throwableResponseBody.asBytes());
        Mono<DataBuffer> mono = Mono.just(buffer);
        return response.writeAndFlushWith(Mono.just(mono));
    }

    public Mono<ServerResponse> getResponse() {
        return status(getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(throwableResponseBody), ResponseBody.class);
    }

    private HttpStatus getHttpStatus() {
        return HttpStatus.resolve(throwableResponseBody.getHttpStatus());
    }
}
