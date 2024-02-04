package com.we.broke.payment.service;

import com.we.broke.app.repository.OrganisationRepository;
import com.we.broke.payment.model.PaymentData;
import com.stripe.model.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.we.broke.payment.service.PaymentDataQueryService.hasSubscriptionsIdsIn;
import static com.we.broke.payment.service.StripeDataService.ORGANISATION_IDS;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
class PaymentDataSyncService {
    private final OrganisationRepository organisationRepository;

    private final PaymentDataModifyService modificationService;
    private final PaymentDataQueryService paymentDataQueryService;
    private final StripeDataService stripeDataService;
    private final StripeSubscriptionService subscriptionManager;

    //TODO: add try catch for timeout issues
    @Scheduled(cron = "${payment.stripe.sync-cron}")
    public void sync() {
        log.info("Starting payment synchronization");
        Set<String> syncedOrgIds = new HashSet<>();
        stripeDataService.iterateOverSubscriptions(subscriptions -> {
            //get and update existing details if status has changed
            Map<String, Subscription> activeSubscriptionsIds = subscriptions.stream().collect(Collectors.toMap(Subscription::getId, Function.identity()));
            Page<PaymentData> paymentDetails =
                    paymentDataQueryService.searchAll(hasSubscriptionsIdsIn(activeSubscriptionsIds.keySet().stream().toList()),
                            Pageable.unpaged());
            paymentDetails.forEach(paymentData -> {
                Subscription subscription = activeSubscriptionsIds.get(paymentData.getSubscriptionId());
                String orgIdString = subscription.getMetadata().get(ORGANISATION_IDS);
                String orgId = orgIdString.substring(1, orgIdString.length() - 1);
                log.info("Updated org: {}", orgId);
                if (subscriptionManager.getPaymentStatus(subscription) != paymentData.getStatus()) {
                    if (StringUtils.isNoneBlank(orgId) && !syncedOrgIds.contains(orgId)) {
                        subscriptionManager.upsertSubscriptionData(subscription);
                    }
                }
                syncedOrgIds.add(orgId);
            });
            //add new subs for not yet synced new orgs
            List<String> newActiveSubs =
                    activeSubscriptionsIds.keySet().stream().filter(s -> paymentDetails.getContent().stream().noneMatch(paymentData -> paymentData.getSubscriptionId().equals(s))).toList();
            newActiveSubs.forEach(s -> {
                Subscription subscription = activeSubscriptionsIds.get(s);
                String orgIdString = subscription.getMetadata().get(ORGANISATION_IDS);
                String orgId = orgIdString.substring(1, orgIdString.length() - 1);
                log.info("New org: {}", orgId);
                if (StringUtils.isNoneBlank(orgId) && !syncedOrgIds.contains(orgId)) {
                    subscriptionManager.upsertSubscriptionData(subscription);
                }
                syncedOrgIds.add(orgId);
            });
        });
        //cancel other org which are not in sub list
        //need at least one ID, 0 is not a start ID so use this if empty
        if (syncedOrgIds.isEmpty()) {
            syncedOrgIds.add("0");
        }
        List<Long> orgsToNotCancel = syncedOrgIds.stream().map(Long::valueOf).toList();
        log.info("Org to not cancel: {}", orgsToNotCancel);
        List<Long> allIdsByIdNotIn = organisationRepository.findAllToCancel(orgsToNotCancel);
        subscriptionManager.cancelPaymentDataForOrgs(allIdsByIdNotIn);
        subscriptionManager.cancelSubscriptionForOrgs(allIdsByIdNotIn);
        log.info("Finished payment synchronization");
    }

//    @Scheduled(cron = "${payment.stripe.credit-reset-cron}")
//    public void creditsResetCheck() {
//        log.info("Starting credit reset checks");
//        modificationService.creditsResetCheck();
//        log.info("Finished credit reset checks");
//    }

}
