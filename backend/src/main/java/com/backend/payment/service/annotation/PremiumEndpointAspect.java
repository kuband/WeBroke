package com.backend.payment.service.annotation;

import com.backend.auth.security.services.UserDetailsImpl;
import com.backend.payment.service.PaymentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PremiumEndpointAspect {

    private final PaymentResolver paymentResolver;

    @Value("${payment.validation.enabled}")
    private boolean paymentValidationEnabled;

    @Pointcut(value = "@annotation(com.backend.payment.service.annotation" +
            ".PremiumEndpoint) && @within(org.springframework.web.bind.annotation.RestController)")
    public void controllerMethods() {
    }

    @Pointcut(value = "@target(com.backend.payment.service.annotation.PremiumEndpoint) &&" +
            " @within(org.springframework.web.bind.annotation.RestController)")
    public void controllerClass() {
    }

    @Pointcut("controllerClass() || controllerMethods()")
    public void controllerAnnotations() {
    }

    @Around("controllerAnnotations()")
    public Object handle(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj = joinPoint.getTarget();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        PremiumEndpoint classAnnotation = AnnotationUtils.findAnnotation(obj.getClass(),
                PremiumEndpoint.class);
        PremiumEndpoint methodAnnotation = AnnotationUtils.findAnnotation(method,
                PremiumEndpoint.class);
        if (Objects.nonNull(classAnnotation) || Objects.nonNull(methodAnnotation)) {
            Optional<List<Long>> nullableCurrentUserOrganisationId = getCurrentUserOrganisationId();
            nullableCurrentUserOrganisationId.ifPresentOrElse(
                    paymentResolver::resolveAccess,
                    () -> {
                        log.warn("Cannot find current users organisation id, denying the access");
                        throw new PaymentException("Cannot access this resource. Pay for the " +
                                "subscription in order to access it.");
                    }
            );
        } else {
            log.warn("Triggered paid endpoint check but no annotation was found");
        }
        // making sure we have the access to the method (in case it is package private or something)
        method.setAccessible(true);
        // if the invoked method throws an exception, we want to rethrow the original exception
        // taken from https://amitstechblog.wordpress.com/2011/07/24/java-proxies-and-undeclaredthrowableexception/
        try {
            return method.invoke(obj, joinPoint.getArgs());
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    public Optional<List<Long>> getCurrentUserOrganisationId() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Object principal =
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetailsImpl) {
                return Optional.ofNullable(((UserDetailsImpl) principal).getOrganisationIds());
            }
        }
        return Optional.empty();
    }

}
