package com.inmaytide.exception.web;

import com.inmaytide.exception.web.domain.DefaultErrorCode;
import com.inmaytide.exception.web.domain.ErrorCode;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * 请求的数据对象不存在或已删除
 *
 * @author inmaytide
 * @since 2020/11/25
 */
public class ObjectNotFoundException extends HttpResponseException {

    @Serial
    private static final long serialVersionUID = 2741299313015836267L;

    private static final ErrorCode DEFAULT_CODE = new DefaultErrorCode("exception_obj_notfound", "没有找到唯一标识为\"{0}\"的数据对象");

    public ObjectNotFoundException() {
        super(HttpStatus.NOT_FOUND, DEFAULT_CODE, "unknown");
    }

    public ObjectNotFoundException(ErrorCode code, String... placeholders) {
        super(HttpStatus.NOT_FOUND, code, placeholders);
    }

    public ObjectNotFoundException(String id) {
        super(HttpStatus.NOT_FOUND, DEFAULT_CODE, id);
    }

    public ObjectNotFoundException(Throwable cause) {
        super(HttpStatus.NOT_FOUND, DEFAULT_CODE, cause, "unknown");
    }

}
