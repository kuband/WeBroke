package com.backend.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.app.model.entity.Organisation;
import com.backend.app.repository.OrganisationRepository;
import com.backend.app.service.UserOrganisationService;
import com.backend.auth.security.services.UserDetailsImpl;
import com.backend.payment.model.PaymentData;
import com.backend.payment.model.PaymentStatus;
import com.backend.payment.model.enumeration.SubscriptionStatus;
import com.backend.payment.repository.PaymentDataRepository;
import com.stripe.model.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.backend.payment.service.PaymentDataQueryService.hasOrganisationIdsIn;
import static com.backend.payment.service.StripeDataService.ORGANISATION_IDS;

@Slf4j
@RequiredArgsConstructor
@Service
public class StripeSubscriptionService {

    private final PaymentDataRepository paymentDataRepository;
    private final PaymentDataQueryService paymentDataQueryService;
    private final StripeDataService stripeDataService;
    private final OrganisationRepository organisationRepository;
    private final UserOrganisationService userOrganisationService;

    @Value("${payment.stripe.basic-product-id}")
    private String basicProductId;
    @Value("${payment.stripe.pro-product-id}")
    private String proProductId;
    @Value("${payment.stripe.enterprise-product-id}")
    private String enterpriseProductId;

    private static LocalDateTime getTrialEnd(Subscription subscription) {
        return Optional.ofNullable(subscription.getTrialEnd())
                .map(Instant::ofEpochSecond)
                .map(instant -> instant.atZone(ZoneId.of("UTC")))
                .map(ZonedDateTime::toLocalDateTime)
                .orElse(null);
    }

    private static List<Long> getOrganisationId(Subscription subscription) {
        try {
            List<Long> organisationIds =
                    new ObjectMapper().readValue(subscription.getMetadata().get(ORGANISATION_IDS)
                            , new TypeReference<>() {
                            });
            TreeSet<Long> sortedIds = new TreeSet<>(organisationIds);
            return sortedIds.stream().toList();
        } catch (JsonProcessingException e) {
            throw new PaymentException("Issue getting orgs from subscription");
        }
    }

    public PaymentStatus getPaymentStatus(Subscription subscription) {
        String trialStatus = "trialing";
        String paidStatus = "active";
        String cancelledStatus = "canceled";
        PaymentStatus status = PaymentStatus.UNPAID;
        List<String> productIds = subscription.getItems().getData().stream().map(subscriptionItem -> subscriptionItem.getPlan().getProduct()).toList();
        if (StringUtils.equalsIgnoreCase(paidStatus, subscription.getStatus()) && productIds.contains(basicProductId)) {
            status = PaymentStatus.BASIC;
        } else if (StringUtils.equalsIgnoreCase(paidStatus, subscription.getStatus()) && productIds.contains(proProductId)) {
            status = PaymentStatus.PRO;
        } else if (StringUtils.equalsIgnoreCase(paidStatus, subscription.getStatus()) && productIds.contains(enterpriseProductId)) {
            status = PaymentStatus.ENTERPRISE;
        } else if (StringUtils.equalsIgnoreCase(cancelledStatus, subscription.getStatus())) {
            status = PaymentStatus.CANCELED;
        } else if (StringUtils.equalsIgnoreCase(trialStatus, subscription.getStatus())) {
            status = PaymentStatus.TRIALING;
        }
        return status;
    }

    @Transactional
    public void createSubscriptionData(Subscription subscription) {
        List<Long> organisationIds = getOrganisationId(subscription);
        if (CollectionUtils.isEmpty(organisationIds)) {
            log.error("No organisation id found in subscription metadata [subscriptionId={}, " +
                            "metadata={}], aborting creation of payment details...",
                    subscription.getId(), subscription.getMetadata());
            return;
        }
        List<Organisation> organisations = organisationRepository.findAllById(organisationIds);
        if (organisations.isEmpty()) {
            log.error("Cannot find profile [organisationId={}], aborting creation of payment " +
                    "details...", organisationIds);
            return;
        }
        PaymentData details = PaymentData.builder()
                .organisations(new HashSet<>(organisations))
                .status(PaymentStatus.PENDING)
                .stripeCustomerId(subscription.getCustomer())
                .build();
        updateSubscriptionData(details, subscription, false);
        organisations.forEach(organisation -> {
            organisation.setSubscriptionStatus(getSubscriptionStatus(details.getStatus()));
        });
        paymentDataRepository.save(details);
        organisationRepository.saveAll(organisations);
    }

    @Transactional
    public void updateSubscriptionPlan(UserDetailsImpl userDetails, long creditAmount, String period, int option) {
        List<Long> organisationIds = userDetails.getOrganisationIds();
        if (organisationIds.isEmpty()) {
            throw new PaymentException("Error while updating subscription plan, contact support");
        }
        stripeDataService.updateSubscription(organisationIds, creditAmount, period, option);
    }

    @Transactional
    public void updatePaymentCard(UserDetailsImpl userDetails, String paymentMethodId) {
        List<Long> organisationIds = userDetails.getOrganisationIds();
        if (organisationIds.isEmpty()) {
            throw new PaymentException("Error while updating payment card, contact support");
        }
        stripeDataService.updatePaymentCard(userDetails, organisationIds, paymentMethodId);
    }

    @Transactional
    public void upsertSubscriptionData(Subscription subscription) {
        List<Long> organisationIds = getOrganisationId(subscription);
        if (CollectionUtils.isEmpty(organisationIds)) {
            log.error("No organisation id found in subscription metadata [subscriptionId={}, " +
                            "metadata={}], aborting upserting of payment details...",
                    subscription.getId(), subscription.getMetadata());
            return;
        }
        Optional<PaymentData> search =
                paymentDataQueryService.search(hasOrganisationIdsIn(organisationIds));
        if (search.isPresent()) {
            updateSubscriptionData(subscription, true);
        } else {
            createSubscriptionData(subscription);
        }
    }

    @Transactional
    public void updateSubscriptionData(Subscription subscription, boolean isSyncing) {
        List<Long> organisationIds = getOrganisationId(subscription);
        if (CollectionUtils.isEmpty(organisationIds)) {
            log.error("No organisation ids found in subscription metadata [subscriptionId={}, " +
                            "metadata={}], aborting update of payment details...",
                    subscription.getId(), subscription.getMetadata());
            return;
        }
        Optional<PaymentData> nullablePaymentDetail =
                paymentDataQueryService.search(hasOrganisationIdsIn(organisationIds));
        nullablePaymentDetail.ifPresentOrElse(
                paymentDetails -> updateSubscriptionData(paymentDetails, subscription, isSyncing),
                () -> log.error("Cannot find payment details [subscriptionId={}, " +
                        "organisationIds={}]", subscription.getId(), organisationIds)
        );
    }

    //TODO: ???
//    public void cancelSubscription(List<Long> organisationIds) {
//        stripeDataService.cancelSubscription(organisationIds);
//    }

    @Transactional
    public void cancelSubscriptionAndUpdatePaymentDetails(UserDetailsImpl userDetails) {
        List<Long> organisationIds = userDetails.getOrganisationIds();
        cancelPaymentDataForOrgs(organisationIds);
        cancelSubscriptionForOrgs(organisationIds);
    }

    @Transactional
    public void cancelPaymentDataForOrgs(List<Long> organisationIds) {
        Page<PaymentData> nullablePaymentDetail =
                paymentDataQueryService.searchAll(hasOrganisationIdsIn(organisationIds), Pageable.unpaged());
        nullablePaymentDetail.forEach(
                paymentDetails -> {
                    stripeDataService.cancelSubscription(organisationIds);
                    paymentDetails.setStatus(PaymentStatus.CANCELED);
                    paymentDataRepository.save(paymentDetails);
                }
        );
    }

    @Transactional
    public void cancelSubscriptionForOrgs(List<Long> organisationIds) {
        List<Organisation> organisations =
                organisationRepository.findAllById(organisationIds);
        organisations.forEach(organisation -> {
            organisation.setSubscriptionStatus(SubscriptionStatus.CANCELED);
        });
        organisationRepository.saveAll(organisations);
    }

    private SubscriptionStatus getSubscriptionStatus(PaymentStatus status) {
        return switch (status) {
            case BASIC -> SubscriptionStatus.BASIC;
            case PRO -> SubscriptionStatus.PRO;
            case ENTERPRISE -> SubscriptionStatus.ENTERPRISE;
            case TRIALING -> SubscriptionStatus.TRIALING;
            default -> SubscriptionStatus.FREE;
        };
    }

    private void updateSubscriptionData(PaymentData paymentData, Subscription subscription,
                                        boolean isSyncing) {
        LocalDateTime trialEnd = getTrialEnd(subscription);
        PaymentStatus status = getPaymentStatus(subscription);
        List<Organisation> organisations =
                organisationRepository.findAllById(paymentData.getOrganisations().stream().map(Organisation::getId).collect(Collectors.toList()));
        if (status != paymentData.getStatus()) {
            organisations.forEach(organisation -> organisation.setSubscriptionStatus(getSubscriptionStatus(status)));
        }
        organisationRepository.saveAll(organisations);
        paymentData.setStatus(status);
        paymentData.setSubscriptionId(subscription.getId());
        paymentData.setTrialsEnd(trialEnd);
        paymentData.setStripeCustomerId(subscription.getCustomer());
        log.info("Updating payment details [paymentDetailsId={}], with the following data " +
                        "[trialsEnd={}, subscriptionId={}, customerId={}, status={}]",
                paymentData.getId(), trialEnd, subscription.getId(), subscription.getCustomer(),
                status);
    }

//    private long getNumberOfCredits(Subscription subscription) {
//        return Objects.requireNonNull(subscription).getItems().getData().stream()
//                .filter(subItem -> productId.equals(subItem.getPrice().getProduct()))
//                .filter(subItem -> priceId.equals(subItem.getPrice().getId()) || trialPriceId
//                .equals(subItem.getPrice().getId()))
//                .mapToLong(SubscriptionItem::getQuantity)
//                .sum();
//    }

}
