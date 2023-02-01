package com.inmaytide.exception.web;

import com.inmaytide.exception.web.domain.DefaultErrorCode;
import com.inmaytide.exception.web.domain.ErrorCode;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * 请求受保护资源时未提供相关凭证
 *
 * @author luomiao
 * @since 2020/11/30
 */
public class UnauthorizedException extends HttpResponseException {

    @Serial
    private static final long serialVersionUID = -8125873136146325022L;

    private static final ErrorCode DEFAULT_CODE = new DefaultErrorCode("exception_unauthorized", "未提供相关凭证");

    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED, DEFAULT_CODE);
    }

    public UnauthorizedException(ErrorCode code, String... placeholders) {
        super(HttpStatus.UNAUTHORIZED, code, placeholders);
    }

    public UnauthorizedException(Throwable cause) {
        super(HttpStatus.UNAUTHORIZED, DEFAULT_CODE, cause);
    }

}
