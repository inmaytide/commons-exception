package com.inmaytide.exception.web;

import com.inmaytide.exception.web.domain.DefaultErrorCode;
import com.inmaytide.exception.web.domain.ErrorCode;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * 请求的资源地址不存在或已删除
 *
 * @author inmaytide
 * @since 2020/11/25
 */
public class PathNotFoundException extends HttpResponseException {

    @Serial
    private static final long serialVersionUID = 4610677337078978200L;

    private static final ErrorCode DEFAULT_CODE = new DefaultErrorCode("exception_path_notfound", "请求的资源地址已删除或不存在");

    public PathNotFoundException() {
        super(HttpStatus.NOT_FOUND, DEFAULT_CODE);
    }

    public PathNotFoundException(ErrorCode code, String... placeholders) {
        super(HttpStatus.NOT_FOUND, code, placeholders);
    }

    public PathNotFoundException(Throwable cause) {
        super(HttpStatus.NOT_FOUND, DEFAULT_CODE, cause);
    }

}
