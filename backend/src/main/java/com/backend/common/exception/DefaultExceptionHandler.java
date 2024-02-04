package com.backend.common.exception;

import com.backend.app.exception.OperationException;
import com.backend.app.exception.UnsupportedOperationException;
import com.backend.auth.exception.EmailException;
import com.backend.auth.exception.OrganisationException;
import com.backend.auth.exception.PasswordException;
import com.backend.auth.exception.TokenException;
import com.backend.payment.service.PaymentException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AuthenticationException.class, UsernameNotFoundException.class,
            OrganisationException.class})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(Exception authException) {
        log.error("Unauthorized error: {}", authException.getMessage());

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage());


        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(body);
    }

    @ExceptionHandler({EmailException.class})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleEmailException(Exception e) {
        log.error("Email error: {}", e.getMessage());

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_BAD_REQUEST);
        body.put("error", "Email error");
        body.put("message", e.getMessage());


        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(body);
    }

    @ExceptionHandler({PasswordException.class})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handlePasswordException(Exception e) {
        log.error("Password error: {}", e.getMessage());

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_BAD_REQUEST);
        body.put("error", "Password error");
        body.put("message", e.getMessage());


        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(body);
    }

    @ExceptionHandler({TokenException.class})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleTokenException(Exception e) {
        log.error("Token error: {}", e.getMessage());

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Token error");
        body.put("message", e.getMessage());


        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(body);
    }

    @ExceptionHandler({UnsupportedOperationException.class})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleUnsupportedOperationException(Exception e) {
        log.error("UnsupportedOperation error: {}", e.getMessage());

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_BAD_REQUEST);
        body.put("error", "UnsupportedOperation error");
        body.put("message", e.getMessage());


        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(body);
    }

    @ExceptionHandler({OperationException.class})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleOperationException(Exception e) {
        log.error("OperationsException error: {}", e.getMessage());

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_EXPECTATION_FAILED);
        body.put("error", "OperationsException error");
        body.put("message", e.getMessage());


        return ResponseEntity.status(HttpServletResponse.SC_EXPECTATION_FAILED).body(body);
    }

    @ExceptionHandler({PaymentException.class})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handlePaymentException(Exception e) {
        log.error("OperationsException error: {}", e.getMessage());

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_PAYMENT_REQUIRED);
        body.put("error", "PaymentException error");
        body.put("message", e.getMessage());


        return ResponseEntity.status(HttpServletResponse.SC_PAYMENT_REQUIRED).body(body);
    }

}
