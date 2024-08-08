package com.zerobase.backend.repository;

import com.zerobase.backend.domain.TeamPurchasePayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamPurchasePaymentRepository extends JpaRepository<TeamPurchasePayment, Long> {
}
