package com.backend.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
public class OperationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public OperationException() {
        super();
    }

    public OperationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public OperationException(final String message) {
        super(message);
    }

    public OperationException(final Throwable cause) {
        super(cause);
    }
}
