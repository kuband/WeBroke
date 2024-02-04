package com.backend.payment.controller;

import com.backend.auth.security.services.UserDetailsImpl;
import com.backend.payment.service.StripeSubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments/update")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class PaymentUpdateController {
    private StripeSubscriptionService stripeSubscriptionService;

    @PostMapping("/subscriptionPlan/{period}/{option}/{creditAmount}")
    void updateSubscriptionPlan(@AuthenticationPrincipal UserDetailsImpl currentUser,
                                @PathVariable String period, @PathVariable int option, @PathVariable long creditAmount) {
        stripeSubscriptionService.updateSubscriptionPlan(currentUser, creditAmount, period, option);
    }

    @PostMapping("/paymentMethod/{paymentMethodId}")
    void updatePaymentCard(@AuthenticationPrincipal UserDetailsImpl currentUser,
                           @PathVariable String paymentMethodId) {
        stripeSubscriptionService.updatePaymentCard(currentUser, paymentMethodId);
    }

    @PostMapping("/cancel")
    void cancelSubscriptionAndUpdatePaymentDetails(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        stripeSubscriptionService.cancelSubscriptionAndUpdatePaymentDetails(currentUser);
    }
}
