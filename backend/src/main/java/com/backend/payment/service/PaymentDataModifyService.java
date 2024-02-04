package com.backend.payment.service;

import com.backend.app.model.entity.Organisation;
import com.backend.payment.model.PaymentData;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.backend.payment.service.PaymentDataQueryService.hasSubscriptionsIdsIn;

@Slf4j
@RequiredArgsConstructor
@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PaymentDataModifyService {
    PaymentDataQueryService paymentDataQueryService;
    StripeDataService stripeDataService;

    //TODO: what is this for?
    @Transactional
    public void cancelPaymentDetails(List<String> subscriptionsIds) {
        Page<PaymentData> paymentDetails =
                paymentDataQueryService.searchAll(hasSubscriptionsIdsIn(subscriptionsIds),
                        Pageable.unpaged());
        List<List<Long>> organisationIds =
                paymentDetails.getContent().stream().map(PaymentData::getOrganisations).map(organisations -> organisations.stream().map(Organisation::getId).collect(Collectors.toList())).toList();
        organisationIds.forEach(stripeDataService::cancelSubscription);
    }
}
