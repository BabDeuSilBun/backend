package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.PurchasePayment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PurchasePaymentRepository extends JpaRepository<PurchasePayment, Long> {

  @Query(value = "SELECT * FROM purchase_payment pp "
      + "WHERE pp.purchase_id = :purchaseId "
      + "ORDER BY pp.created_at DESC LIMIT 1", nativeQuery = true)
  Optional<PurchasePayment> findLastPurchasePayment(Long purchaseId);
}
