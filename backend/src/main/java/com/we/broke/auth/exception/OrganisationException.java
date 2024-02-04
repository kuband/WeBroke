package com.we.broke.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class OrganisationException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  public OrganisationException() {
    super();
  }

  public OrganisationException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public OrganisationException(final String message) {
    super(message);
  }

  public OrganisationException(final Throwable cause) {
    super(cause);
  }
}
