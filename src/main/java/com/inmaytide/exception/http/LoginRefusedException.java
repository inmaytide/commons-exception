package com.inmaytide.exception.http;

import org.springframework.http.HttpStatus;

public class LoginRefusedException extends HttpResponseException {

    private static final String DEFAULT_CODE = "err_login_refused";

    public LoginRefusedException() {
        super(HttpStatus.FORBIDDEN, DEFAULT_CODE);
    }

}
