package com.backend.payment.model;

//TODO: add CUSTOM here and stripe
public enum PaymentStatus {
    BASIC,
    PRO,
    ENTERPRISE,
    TRIALING,
    CANCELED,
    PENDING,
    UNPAID

}
