package com.zerobase.backend.repository;

import com.zerobase.backend.domain.PurchasePayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasePaymentRepository extends JpaRepository<PurchasePayment, Long> {
}
