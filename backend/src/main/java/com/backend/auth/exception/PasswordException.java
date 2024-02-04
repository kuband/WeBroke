package com.backend.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public PasswordException() {
        super();
    }

    public PasswordException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PasswordException(final String message) {
        super(message);
    }

    public PasswordException(final Throwable cause) {
        super(cause);
    }
}
