package com.inmaytide.exception.web;

import com.inmaytide.exception.web.domain.DefaultErrorCode;
import com.inmaytide.exception.web.domain.ErrorCode;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * 请求资源时已提供相关凭证但是没有权限(或其他原因)被拒绝访问
 *
 * @author inmaytide
 * @since 2020/12/01
 */
public class AccessDeniedException extends HttpResponseException {

    @Serial
    private static final long serialVersionUID = -1949945016002669108L;
    
    private static final ErrorCode DEFAULT_CODE = new DefaultErrorCode("exception_access_denied", "拒绝访问");

    public AccessDeniedException() {
        super(HttpStatus.FORBIDDEN, DEFAULT_CODE);
    }

    public AccessDeniedException(ErrorCode code, String... placeholders) {
        super(HttpStatus.FORBIDDEN, code, placeholders);
    }

    public AccessDeniedException(Throwable cause) {
        super(HttpStatus.FORBIDDEN, DEFAULT_CODE, cause);
    }

}
