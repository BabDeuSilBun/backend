package com.zerobase.backend.repository;

import com.zerobase.backend.domain.TeamOrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamOrderPaymentRepository extends JpaRepository<TeamOrderPayment, Long> {
}
