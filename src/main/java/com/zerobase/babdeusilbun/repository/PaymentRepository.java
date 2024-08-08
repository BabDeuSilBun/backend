package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
