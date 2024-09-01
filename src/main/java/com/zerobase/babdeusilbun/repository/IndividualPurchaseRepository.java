package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.IndividualPurchase;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Menu;
import com.zerobase.babdeusilbun.domain.Purchase;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface IndividualPurchaseRepository extends JpaRepository<IndividualPurchase, Long> {

  @EntityGraph(attributePaths = {"purchase", "menu"})
  Page<IndividualPurchase> findAllByPurchase(Purchase purchase, Pageable pageable);

  @EntityGraph(attributePaths = {"purchase", "menu"})
  List<IndividualPurchase> findAllByPurchase(Purchase purchase);

  boolean existsAllByMenuAndPurchase(Menu menu, Purchase purchase);

  Optional<IndividualPurchase> findAllById(Long indiviualPurchaseId);

  @Query("select COALESCE(sum(ip.paymentPrice), 0) "
        + "from IndividualPurchase ip "
        + "join Purchase p on ip.purchase = p "
        + "where p = :purchase ")
  Long getParticipantTotalPrice(Purchase purchase);
}
