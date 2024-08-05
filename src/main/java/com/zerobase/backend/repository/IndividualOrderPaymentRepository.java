package com.zerobase.backend.repository;

import com.zerobase.backend.domain.IndividualOrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndividualOrderPaymentRepository extends JpaRepository<IndividualOrderPayment, Long> {
}
