package com.backend.payment.service.annotation;

import com.backend.payment.service.PaymentException;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation is used for guarding an endpoint from unpaid access.
 * If the user stored in {@link org.springframework.security.core.context.SecurityContext} didn't
 * paid
 * for the access to the app then this endpoint will throw {@link PaymentException}.
 * Annotation can be used on the class level or on a method level.
 * Class annotation will apply to all mappings within the controller.
 * Method annotation will apply to particular mapping (method).
 * Works only if the class is annotated with
 * {@link org.springframework.web.bind.annotation.RestController}
 */
@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface PremiumEndpoint {
}
