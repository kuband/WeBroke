package com.we.broke.payment.controller;

import com.we.broke.auth.security.services.UserDetailsImpl;
import com.we.broke.payment.service.StripeSessionsService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
class PaymentsCheckoutSessionController {
    StripeSessionsService stripeSessionsService;

    @PostMapping("/checkout-session/{period}/{option}/{creditAmount}")
    String createTrialCheckoutSession(@AuthenticationPrincipal UserDetailsImpl currentUser,
                                      @PathVariable String period, @PathVariable int option, @PathVariable long creditAmount) {
        return stripeSessionsService.createTrialOrPortalRedirectSession(currentUser, period, option, creditAmount);
    }
}
