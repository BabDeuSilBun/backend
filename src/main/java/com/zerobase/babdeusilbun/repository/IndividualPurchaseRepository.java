package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.IndividualPurchase;
import com.zerobase.babdeusilbun.domain.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndividualPurchaseRepository extends JpaRepository<IndividualPurchase, Long> {

  @EntityGraph(attributePaths = {"purchase", "menu"})
  Page<IndividualPurchase> findAllByPurchase(Purchase purchase, Pageable pageable);
}
