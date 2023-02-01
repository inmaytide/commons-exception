package com.inmaytide.exception.web;

import com.inmaytide.exception.web.domain.DefaultErrorCode;
import com.inmaytide.exception.web.domain.ErrorCode;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * 请求参数验证失败
 *
 * @author inmaytide
 * @since 2020/11/25
 */
public class BadRequestException extends HttpResponseException {

    @Serial
    private static final long serialVersionUID = -6499317868370748373L;

    private static final ErrorCode DEFAULT_CODE = new DefaultErrorCode("exception_bad_request", "请求参数验证失败");

    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST, DEFAULT_CODE);
    }

    public BadRequestException(Throwable cause) {
        super(HttpStatus.BAD_REQUEST, DEFAULT_CODE, cause);
    }

    public BadRequestException(ErrorCode code, String... placeholders) {
        super(HttpStatus.BAD_REQUEST, code, placeholders);
    }

}
