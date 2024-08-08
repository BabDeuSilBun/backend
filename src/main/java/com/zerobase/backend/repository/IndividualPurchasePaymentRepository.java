package com.zerobase.backend.repository;

import com.zerobase.backend.domain.IndividualPurchasePayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndividualPurchasePaymentRepository extends JpaRepository<IndividualPurchasePayment, Long> {
}
