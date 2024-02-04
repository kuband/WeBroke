package com.backend.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.app.exception.OperationException;
import com.backend.auth.security.services.UserDetailsImpl;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import com.stripe.param.checkout.SessionCreateParams.SubscriptionData;
import com.stripe.param.checkout.SessionCreateParams.SubscriptionData.TrialSettings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeSet;

import static com.backend.payment.service.PaymentDataQueryService.hasOrganisationIdsIn;
import static com.backend.payment.service.StripeDataService.ORGANISATION_IDS;
import static com.stripe.param.checkout.SessionCreateParams.Mode.SUBSCRIPTION;
import static com.stripe.param.checkout.SessionCreateParams.SubscriptionData.TrialSettings.EndBehavior.MissingPaymentMethod.CANCEL;
import static com.stripe.param.checkout.SessionCreateParams.SubscriptionData.TrialSettings.EndBehavior.builder;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripeSessionsService {
    private final RequestOptions requestOptions;
    private final StripeDataService stripeDataService;
    private final PaymentDataQueryService paymentDataQueryService;

    @Value("${payment.trial.period-days}")
    private long trialPeriodInDays;

    @Value("${payment.stripe.basic-monthly-price-id}")
    private String basicMonthlySubscriptionPriceId;
    @Value("${payment.stripe.pro-monthly-price-id}")
    private String proMonthlySubscriptionPriceId;
    @Value("${payment.stripe.enterprise-monthly-price-id}")
    private String enterpriseMonthlySubscriptionPriceId;
    @Value("${payment.stripe.basic-yearly-price-id}")
    private String basicYearlySubscriptionPriceId;
    @Value("${payment.stripe.pro-yearly-price-id}")
    private String proYearlySubscriptionPriceId;
    @Value("${payment.stripe.enterprise-yearly-price-id}")
    private String enterpriseYearlySubscriptionPriceId;

    @Value("${payment.stripe.checkout.success-url}")
    private String stripeCheckoutSuccessUrl;

    @Value("${payment.stripe.checkout.cancel-url}")
    private String stripeCheckoutCancelUrl;

    public String createTrialOrPortalRedirectSession(UserDetailsImpl currentUser,
                                                     String period,
                                                     int option,
                                                     long creditAmount) {
        SessionCreateParams params = null;
        List<Long> organisationIds = currentUser.getOrganisationIds();
        try {
            StripeDataService.StripeCustomer customer =
                    stripeDataService.getOrCreateCustomer(currentUser, organisationIds);
            SubscriptionData subscriptionData = createSubscriptionData(organisationIds, period, option);
            log.info("Trying to create trial/portal checkout session [organisationIds={}, " +
                            "customerId={}, creditAmount={}]", organisationIds, customer.id(),
                    creditAmount);
            TreeSet<Long> sortedIds = new TreeSet<>(organisationIds);
            params = SessionCreateParams.builder()
                    .addLineItem(createLineItem(creditAmount, period, option))
                    .setMode(SUBSCRIPTION)
                    .setSuccessUrl(stripeCheckoutSuccessUrl)
                    .setCancelUrl(stripeCheckoutCancelUrl)
                    .putMetadata(ORGANISATION_IDS,
                            new ObjectMapper().writeValueAsString(sortedIds))
                    .setCustomer(customer.id())
                    .setSubscriptionData(subscriptionData)
                    //dont ask for payment details
                    .setPaymentMethodCollection(SessionCreateParams.PaymentMethodCollection.IF_REQUIRED)
                    .build();
        } catch (JsonProcessingException e) {
            throw new PaymentException("Issue creating session");
        }
        try {
            Session session = Session.create(params, requestOptions);
            log.info("Created a checkout session [sessionId={}, organisationIds={}, " +
                    "currentUserId={}]", session.getId(), organisationIds, currentUser.getId());
            return session.getUrl();
        } catch (StripeException e) {
            log.error("Cannot create checkout session [organisationIds={}, currentUserId={}]",
                    organisationIds, currentUser.getId(), e);
            throw new PaymentException("Error while creating checkout session, contact support");
        }
    }

    private SubscriptionData createSubscriptionData(List<Long> organisationIds, String period, long option) throws JsonProcessingException {
        TreeSet<Long> sortedIds = new TreeSet<>(organisationIds);
        return paymentDataQueryService.exists(hasOrganisationIdsIn(organisationIds)) || period.equalsIgnoreCase("yearly") || option > 1 ?
                SubscriptionData.builder()
                        .putMetadata(ORGANISATION_IDS,
                                new ObjectMapper().writeValueAsString(sortedIds))
                        .build() :
                SubscriptionData.builder()
                        .putMetadata(ORGANISATION_IDS,
                                new ObjectMapper().writeValueAsString(sortedIds))
                        .setTrialSettings(
                                TrialSettings.builder()
                                        .setEndBehavior(builder().setMissingPaymentMethod(CANCEL).build())
                                        .build()
                        )
                        .setTrialPeriodDays(trialPeriodInDays)
                        .build();
    }

    private LineItem createLineItem(long creditAmount, String period, int option) {
        return switch (period) {
            case "monthly" -> LineItem.builder()
                    .setPrice(switch (option) {
                        case 1 -> creditAmount < 2 ? basicMonthlySubscriptionPriceId : "";
                        case 2 -> creditAmount < 11 ? proMonthlySubscriptionPriceId : "";
                        case 3 -> enterpriseMonthlySubscriptionPriceId;
                        default -> throw new OperationException("Monthly option type not supported");
                    })
                    .setQuantity(creditAmount)
                    .build();
            case "yearly" -> LineItem.builder()
                    .setPrice(switch (option) {
                        case 1 -> creditAmount < 2 ? basicYearlySubscriptionPriceId : "";
                        case 2 -> creditAmount < 11 ? proYearlySubscriptionPriceId : "";
                        case 3 -> enterpriseYearlySubscriptionPriceId;
                        default -> throw new OperationException("Yearly option type not supported");
                    })
                    .setQuantity(creditAmount)
                    .build();
            default -> throw new OperationException("Period not supported");
        };
    }

}
