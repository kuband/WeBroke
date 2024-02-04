package com.we.broke.payment.repository;

import com.we.broke.payment.model.PaymentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PaymentDataRepository extends JpaRepository<PaymentData, Long>,
        JpaSpecificationExecutor<PaymentData> {

}
