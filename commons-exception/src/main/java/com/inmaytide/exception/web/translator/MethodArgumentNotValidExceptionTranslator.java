package com.inmaytide.exception.web.translator;

import com.inmaytide.exception.web.BadRequestException;
import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.domain.DefaultErrorCode;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author inmaytide
 * @since 2023/5/18
 */
public class MethodArgumentNotValidExceptionTranslator extends AbstractHttpExceptionTranslator {
    @Override
    protected Optional<HttpResponseException> execute(Throwable e) {
        if (e instanceof MethodArgumentNotValidException ex) {
            BindingResult res = ex.getBindingResult();
            String message = res.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(","));
            return Optional.of(new BadRequestException(new DefaultErrorCode("exception_bad_request", message)));
        }
        return Optional.empty();
    }

    @Override
    public int getOrder() {
        return 5;
    }
}
