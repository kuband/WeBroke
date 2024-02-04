package com.backend.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedOperationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnsupportedOperationException() {
        super();
    }

    public UnsupportedOperationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnsupportedOperationException(final String message) {
        super(message);
    }

    public UnsupportedOperationException(final Throwable cause) {
        super(cause);
    }
}
