package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.PurchasePayment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PurchasePaymentRepository extends JpaRepository<PurchasePayment, Long> {

}
