package com.we.broke.payment.service.annotation;

import com.we.broke.payment.model.PaymentData;
import com.we.broke.payment.model.PaymentStatus;
import com.we.broke.payment.service.PaymentDataQueryService;
import com.we.broke.payment.service.PaymentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.we.broke.payment.service.PaymentDataQueryService.hasOrganisationIdsIn;

@Slf4j
@Service
@RequiredArgsConstructor
public class StandardPaymentResolver implements PaymentResolver {

    private static final List<PaymentStatus> PAID_STATUSES = List.of(PaymentStatus.TRIALING,
            PaymentStatus.BASIC, PaymentStatus.PRO, PaymentStatus.ENTERPRISE);

    private final PaymentDataQueryService paymentDataQueryService;

    @Value("${payment.validation.enabled}")
    private boolean paymentValidationEnabled;

    @Override
    public void resolveAccess(List<Long> organisationIds) {
        if (paymentValidationEnabled) {
            log.debug("Checking payment access for organisationIds [organisationIds={}]",
                    organisationIds);
            PaymentData paymentData =
                    paymentDataQueryService.search(hasOrganisationIdsIn(organisationIds)).orElseThrow(() -> new PaymentException("Cannot find any payment information. Update your payment details."));
            log.debug("Checking payment access for organisationIds [organisationIds={}] with " +
                    "payment details [paymentDetailsId={}, paymentStatus={}]", organisationIds,
                    paymentData.getId(), paymentData.getStatus());
            if (!PAID_STATUSES.contains(paymentData.getStatus())) {
                throw new PaymentException("Incorrect payment status. Check payment status.");
            }
        } else {
            log.info("Payment validation is disabled, skipping the check");
        }
    }

//    @Override
//    public boolean hasAccessOrganisation(long organisationId) {
//        if (paymentValidationEnabled) {
//            io.enforcio.backend.domain.payment.web.model.PaymentDetails paymentDetails =
//            paymentDetailsQueryService.search(hasOrganisationId(organisationId)).orElseThrow(()
//            -> new io.enforcio.backend.domain.payment.service.PaymentException("Cannot find any
//            payment information. Update your payment details."));
//            log.debug("Checking payment access for the organisation [organisationId={}] with
//            payment details [paymentDetailsId={}, paymentStatus={}]", organisationId,
//            paymentDetails.getId(), paymentDetails.getStatus());
//            return PAID_STATUSES.contains(paymentDetails.getStatus());
//        } else {
//            log.info("Payment validation is disabled, skipping the check");
//        }
//        return true;
//    }
//
//    @Override
//    public boolean hasAccessUser(long userId) {
//        if (paymentValidationEnabled) {
//            io.enforcio.backend.domain.payment.web.model.PaymentDetails paymentDetails =
//            paymentDetailsQueryService.search(hasTenantId(userId)).orElseThrow(() -> new io
//            .enforcio.backend.domain.payment.service.PaymentException("Cannot find any payment
//            information. Update your payment details."));
//            log.debug("Checking payment access for the user [userId={}] with payment details
//            [paymentDetailsId={}, paymentStatus={}]", userId, paymentDetails.getId(),
//            paymentDetails.getStatus());
//            return PAID_STATUSES.contains(paymentDetails.getStatus());
//        } else {
//            log.info("Payment validation is disabled, skipping the check");
//        }
//        return true;
//    }

}
