package com.backend.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  public TokenException() {
    super();
  }

  public TokenException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public TokenException(final String message) {
    super(message);
  }

  public TokenException(final Throwable cause) {
    super(cause);
  }
}
