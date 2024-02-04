package com.backend.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.app.exception.OperationException;
import com.backend.auth.security.services.UserDetailsImpl;
import com.backend.payment.model.PaymentData;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.RequestOptions;
import com.stripe.param.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Consumer;

import static com.backend.payment.service.PaymentDataQueryService.hasOrganisationIdsIn;

@Slf4j
@Service
@RequiredArgsConstructor
class StripeDataService {

    public static final String ORGANISATION_IDS = "organisation_ids";

    private static final long PAGE_LIMIT = 50L;

    private final PaymentDataQueryService paymentDataQueryService;
    private final RequestOptions requestOptions;

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

    public void iterateOverSubscriptions(Consumer<List<Subscription>> subscriptionConsumer) {
        try {
            //update as type of accounts
            String query = "status:'active' OR status:'trialing'";
            SubscriptionSearchResult search = Subscription.search(
                    new SubscriptionSearchParams.Builder()
                            .setQuery(query)
                            .setLimit(PAGE_LIMIT)
                            .build(),
                    requestOptions
            );
            String nextPage = search.getNextPage();
            subscriptionConsumer.accept(search.getData());
            while (search.getHasMore()) {
                search = Subscription.search(
                        new SubscriptionSearchParams.Builder()
                                .setQuery(query)
                                .setLimit(50L)
                                .setPage(nextPage)
                                .build(),
                        requestOptions
                );
                nextPage = search.getNextPage();
                subscriptionConsumer.accept(search.getData());
            }
        } catch (StripeException e) {
            throw new PaymentException(e.getMessage());
        }
    }

    public void updateSubscription(List<Long> organisationIds, long creditAmount, String period, int option) {
        Optional<Subscription> nullableSubscription = findSubscriptionInStripe(organisationIds);
        if (nullableSubscription.isEmpty()) {
            log.warn("Cannot find a subscription for organisation [organisationIds={}]",
                    organisationIds);
            return;
        }
        Subscription subscription = nullableSubscription.get();
        Optional<SubscriptionItem> nullableSubscriptionItem =
                subscription.getItems().getData().stream()
                        .findFirst();
        if (nullableSubscriptionItem.isEmpty()) {
            log.warn("Cannot find a subscription item for organisation subscription " +
                            "[organisationId={}, subscriptionId={}]", organisationIds,
                    subscription.getId());
            return;
        }

        SubscriptionUpdateParams subscriptionUpdateParams = new SubscriptionUpdateParams.Builder()
                .addItem(
                        createUpdateItem(nullableSubscriptionItem.get().getId(), creditAmount, period, option)
                )
                .setProrationBehavior(SubscriptionUpdateParams.ProrationBehavior.ALWAYS_INVOICE)
                .build();

        try {
            subscription.update(subscriptionUpdateParams, requestOptions);
            log.info("Updating the quantity of the subscription [organisationIds={}, " +
                            "subscriptionId={}, newQuantity={}]", organisationIds,
                    subscription.getId(),
                    creditAmount);
        } catch (StripeException e) {
            log.error("Exception updating the quantity of the subscription [organisationIds={}, " +
                            "subscriptionId={}, newQuantity={}]", organisationIds,
                    subscription.getId(),
                    creditAmount, e);
        }
    }

    public Optional<Subscription> findSubscriptionInStripe(List<Long> organisationIds) {
        TreeSet<Long> sortedIds = new TreeSet<>(organisationIds);
        try {
            SubscriptionSearchResult search = Subscription.search(
                    new SubscriptionSearchParams.Builder()
                            .setLimit(1L)
                            .setQuery("metadata[\"%s\"]:\"%s\"".formatted(ORGANISATION_IDS,
                                    sortedIds))
                            .build(),
                    requestOptions
            );
            return search.getData().stream().findFirst();
        } catch (StripeException e) {
            log.error("Exception while searching a stripe subscription [organisationIdd={}]",
                    sortedIds, e);
            return Optional.empty();
        }
    }

    @Async
    public void cancelSubscription(List<Long> organisationIds) {
        log.info("Trying to cancel subscription for the organisation [subscriptionsIds={}]",
                organisationIds);
        Optional<Subscription> nullableSubscription = findSubscriptionInStripe(organisationIds);
        if (nullableSubscription.isEmpty()) {
            log.warn("Cannot find a subscription for organisation [organisationIds={}]",
                    organisationIds);
            return;
        }
        Subscription subscription = nullableSubscription.get();
        if (subscription.getStatus().equalsIgnoreCase("canceled")) {
            log.warn("Subscription already canceled [organisationIds={}]",
                    organisationIds);
            return;
        }
        try {
            subscription.cancel(
                    new SubscriptionCancelParams.Builder()
                            .setCancellationDetails(
                                    new SubscriptionCancelParams.CancellationDetails.Builder()
                                            .setComment("Cancelled in the app")
                                            .build())
                            .build(),
                    requestOptions
            );
            log.info("User cancelled subscription for the organisation [subscriptionId={}, " +
                    "organisationIds={}]", subscription.getId(), organisationIds);
        } catch (StripeException e) {
            log.error("Exception when cancelling the subscription [subscriptionId={}, " +
                    "organisationIds={}]", subscription.getId(), organisationIds, e);
        }
    }

    @Async
    public void updatePaymentCard(UserDetailsImpl currentUser, List<Long> organisationIds,
                                  String paymentMethodId) {
        log.info("Trying to update payment card for the organisations [organisationIds={}]",
                organisationIds);
        try {
            StripeCustomer stripeCustomer = getOrCreateCustomer(currentUser, organisationIds);
            PaymentMethod resource = PaymentMethod.retrieve(paymentMethodId, requestOptions);
            PaymentMethodAttachParams params = PaymentMethodAttachParams.builder()
                    .setCustomer(stripeCustomer.id)
                    .build();
            resource.attach(params, requestOptions);

            Optional<Subscription> nullableSubscription = findSubscriptionInStripe(organisationIds);
            if (nullableSubscription.isEmpty()) {
                log.warn("Cannot find a subscription for organisation [organisationIds={}]",
                        organisationIds);
                return;
            }
            Subscription subscription = nullableSubscription.get();

            SubscriptionUpdateParams subscriptionUpdateParams =
                    new SubscriptionUpdateParams.Builder()
                            .setDefaultPaymentMethod(paymentMethodId)
                            .build();

            try {
                subscription.update(subscriptionUpdateParams, requestOptions);
                log.info("User updated payment card for the organisation [organisationIds={}]",
                        organisationIds);
            } catch (StripeException e) {
                log.error("Exception when updating payment card for the organisation " +
                        "[organisationId={}]", organisationIds, e);
                throw new PaymentException("Failed to update the payment method. Please try again" +
                        " later.");
            }
        } catch (StripeException e) {
            log.error("Exception when updating payment card for the organisation " +
                    "[organisationId={}]", organisationIds, e);
            throw new PaymentException("Failed to update the payment method. Please try again " +
                    "later.");
        }
    }

    public StripeCustomer getOrCreateCustomer(UserDetailsImpl currentUser,
                                              List<Long> organisationIds) {
        log.debug("Trying to retrieve a stripe customer [organisationIds={}, currentUserId={}]",
                organisationIds, currentUser.getId());
        return paymentDataQueryService.search(hasOrganisationIdsIn(organisationIds))
                .map(PaymentData::getStripeCustomerId)
                .flatMap(this::findCustomerInStripe)
                .or(() -> findCustomerInStripe(organisationIds))
                .or(() -> createCustomer(currentUser, organisationIds))
                .orElseThrow(() -> new PaymentException(("Cannot find/create a customer for user " +
                        "[id=%s] in organisation [ids=%s]").formatted(currentUser.getId(),
                        organisationIds)));
    }

    private Optional<StripeCustomer> findCustomerInStripe(String customerId) {
        try {
            Customer customer = Customer.retrieve(customerId, requestOptions);
            return Optional.of(new StripeCustomer(customer.getId(), customer.getEmail()));
        } catch (StripeException e) {
            log.error("Exception while searching a stripe customer [customerId={}]", customerId, e);
            return Optional.empty();
        }
    }

    private Optional<StripeCustomer> findCustomerInStripe(List<Long> organisationIds) {
        TreeSet<Long> sortedIds = new TreeSet<>(organisationIds);
        try {
            CustomerSearchResult search = Customer.search(
                    new CustomerSearchParams.Builder()
                            .setLimit(1L)
                            .setQuery("metadata[\"%s\"]:\"%s\"".formatted(ORGANISATION_IDS,
                                    sortedIds))
                            .build(),
                    requestOptions
            );
            return search.getData().stream().findFirst().map(customer -> new StripeCustomer(customer.getId(), customer.getEmail()));
        } catch (StripeException e) {
            log.error("Exception while searching a stripe customer [organisationId={}]",
                    organisationIds, e);
            return Optional.empty();
        }
    }

    private Optional<StripeCustomer> createCustomer(UserDetailsImpl currentUser,
                                                    List<Long> organisationIds) {
        try {
            String email = currentUser.getEmail();
            TreeSet<Long> sortedIds = new TreeSet<>(organisationIds);
            log.info("Creating a new stripe customer [email={}, organisationIds={}]", email,
                    sortedIds);
            Customer customer = Customer.create(new CustomerCreateParams.Builder()
                            .setEmail(email)
                            .putMetadata(ORGANISATION_IDS,
                                    new ObjectMapper().writeValueAsString(sortedIds))
                            .build(),
                    requestOptions
            );
            log.info("Successfully created a stripe customer [email={}, organisationIds={}, " +
                    "customerId={}]", email, sortedIds, customer.getId());
            return Optional.of(new StripeCustomer(customer.getId(), customer.getEmail()));
        } catch (StripeException e) {
            log.error("Exception while creating a stripe customer [currentUserId={}, " +
                    "organisationIds={}]", currentUser.getId(), organisationIds, e);
            return Optional.empty();
        } catch (JsonProcessingException e) {
            throw new PaymentException("Issue creating customer");
        }
    }

    private SubscriptionUpdateParams.Item createUpdateItem(String subId, long creditAmount, String period, int option) {
        return switch (period) {
            case "monthly" -> SubscriptionUpdateParams.Item.builder()
                    .setId(subId)
                    .setPrice(switch (option) {
                        case 1 -> creditAmount < 2 ? basicMonthlySubscriptionPriceId : "";
                        case 2 -> creditAmount < 11 ? proMonthlySubscriptionPriceId : "";
                        case 3 -> enterpriseMonthlySubscriptionPriceId;
                        default -> throw new OperationException("Monthly option type not supported");
                    })
                    .setQuantity(creditAmount)
                    .build();
            case "yearly" -> SubscriptionUpdateParams.Item.builder()
                    .setId(subId)
                    .setPrice(switch (option) {
                        case 1 -> creditAmount < 2 ? basicYearlySubscriptionPriceId : "";
                        case 2 -> creditAmount < 11 ? proYearlySubscriptionPriceId : "";
                        case 3 -> enterpriseYearlySubscriptionPriceId;
                        default -> throw new OperationException("Monthly option type not supported");
                    })
                    .setQuantity(creditAmount)
                    .build();
            default -> throw new OperationException("Period not supported");
        };
    }

    public record StripeCustomer(String id, String email) {
    }


}
