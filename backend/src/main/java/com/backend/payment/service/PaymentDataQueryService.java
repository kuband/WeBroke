package com.backend.payment.service;


import com.backend.payment.model.PaymentData;
import com.backend.payment.repository.PaymentDataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PaymentDataQueryService {
    PaymentDataRepository paymentDataRepository;

    public static Specification<PaymentData> hasSubscriptionsIdsIn(List<String> subscriptionIds) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get("subscriptionId").in(subscriptionIds);
    }

    public static Specification<PaymentData> hasOrganisationId(long id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(
                "organisations").get("id"), id);
    }

    public static Specification<PaymentData> hasOrganisationIdsIn(List<Long> ids) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get("organisations").get("id").in(ids);
    }

    @Transactional
    public Optional<PaymentData> search(Specification<PaymentData> specification) {
        return paymentDataRepository.findOne(specification);
    }

    @Transactional
    public boolean exists(Specification<PaymentData> specification) {
        return paymentDataRepository.exists(specification);
    }

    @Transactional
    public Page<PaymentData> searchAll(Specification<PaymentData> specification,
                                       Pageable pageable) {
        return paymentDataRepository.findAll(specification, pageable);
    }

}
