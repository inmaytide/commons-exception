package com.inmaytide.exception.web;

import com.inmaytide.exception.web.domain.DefaultErrorCode;
import com.inmaytide.exception.web.domain.ErrorCode;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * 请求受保护资源时提供的相关凭证错误或已过期
 *
 * @author inmaytide
 * @since 2020/11/26
 */
public class BadCredentialsException extends HttpResponseException {

    @Serial
    private static final long serialVersionUID = 3394677497571389704L;

    private static final ErrorCode DEFAULT_CODE = new DefaultErrorCode("exception_bad_credentials", "相关凭证错误或已过期");

    public BadCredentialsException() {
        super(HttpStatus.FORBIDDEN, DEFAULT_CODE);
    }

    public BadCredentialsException(Throwable cause) {
        super(HttpStatus.FORBIDDEN, DEFAULT_CODE, cause);
    }

    public BadCredentialsException(ErrorCode code, String... placeholders) {
        super(HttpStatus.FORBIDDEN, code, placeholders);
    }

}
