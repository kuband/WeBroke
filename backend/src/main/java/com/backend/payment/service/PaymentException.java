package com.backend.payment.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
public class PaymentException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public PaymentException() {
        super();
    }

    public PaymentException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PaymentException(final String message) {
        super(message);
    }

    public PaymentException(final Throwable cause) {
        super(cause);
    }
}
