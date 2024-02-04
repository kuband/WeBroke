package com.backend.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public EmailException() {
        super();
    }

    public EmailException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EmailException(final String message) {
        super(message);
    }

    public EmailException(final Throwable cause) {
        super(cause);
    }
}
