
package com.we.broke.payment.controller;

import com.we.broke.payment.service.PaymentException;
import com.we.broke.payment.service.StripeSubscriptionService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments/webhook")
class PaymentWebhookController {

    private static final String SIGNATURE_HEADER = "Stripe-Signature";
    private final StripeSubscriptionService subscriptionManager;
    @Value("${payment.stripe.webhook-secret}")
    private String webhookSecret;

    private static Optional<StripeObject> deserializeStripeObject(Event event) {
        return event.getDataObjectDeserializer().getObject();
    }

    @PostMapping
    @ResponseStatus(OK)
    void consumePaymentEvent(@RequestBody String payload,
                             @RequestHeader(SIGNATURE_HEADER) String signatureHeader) {
        Event event = getEventFromPayload(payload, signatureHeader);
        // Deserialize the nested object inside the event
        Optional<StripeObject> stripeObjectMaybe = deserializeStripeObject(event);
        log.info("Received [{}] payment event, processing", event.getType());
        if (stripeObjectMaybe.isEmpty()) {
            log.error("Received null stripeObject in webhook callback, aborting execution...");
            return;
        }
        StripeObject stripeObject = stripeObjectMaybe.get();
        switch (event.getType()) {
            case "customer.subscription.deleted", "customer.subscription.trial_will_end",
                    "customer.subscription.updated" -> {
                Subscription subscription = (Subscription) stripeObject;
                subscriptionManager.updateSubscriptionData(subscription, false);
            }
            case "customer.subscription.created" -> {
                Subscription subscription = (Subscription) stripeObject;
                subscriptionManager.createSubscriptionData(subscription);
            }
            default -> log.info("Unhandled event type [{}]", event.getType());
        }
    }

    private Event getEventFromPayload(String payload, String signatureHeader) {
        try {
            return Webhook.constructEvent(payload, signatureHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            log.error("Cannot consume payload, signature is invalid", e);
            throw new PaymentException("Cannot consume payload, signature is invalid");
        }
    }

}
