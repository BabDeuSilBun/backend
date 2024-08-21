package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.TeamPurchasePayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamPurchasePaymentRepository extends JpaRepository<TeamPurchasePayment, Long> {

  @Query("select tp "
      + "from TeamPurchasePayment tpp "
      + "join TeamPurchase tp on tpp.teamPurchase = tp "
      + "where tp.meeting = :meeting")
  Page<TeamPurchasePayment> findByMeeting(Meeting meeting, Pageable pageable);


}
