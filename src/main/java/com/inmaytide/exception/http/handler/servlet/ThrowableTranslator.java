package com.inmaytide.exception.http.handler.servlet;

import com.inmaytide.exception.http.domain.ResponseBody;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author luomiao
 * @since 2019/09/05
 */
class ThrowableTranslator {

    private final ResponseBody body;

    ThrowableTranslator(Throwable throwable, String url) {
        this.body = ResponseBody.newBuilder().throwable(throwable).url(url).build();
    }

    void write(HttpServletResponse response) throws IOException {
        response.sendError(body.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (OutputStream os = response.getOutputStream()) {
            os.write(body.asBytes());
            os.flush();
        }
    }

}
