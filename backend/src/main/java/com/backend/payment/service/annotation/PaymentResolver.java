package com.backend.payment.service.annotation;

import com.backend.payment.service.PaymentException;

import java.util.List;

public interface PaymentResolver {

    /**
     * Throws {@link PaymentException} if the user does not paid for the access or cannot find
     * any payment details for the user.
     * Otherwise, does nothing.
     */
    void resolveAccess(List<Long> organisationId);

//    boolean hasAccessOrganisation(long organisationId);
//
//    boolean hasAccessUser(long currentUserId);

}
