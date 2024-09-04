package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.IndividualPurchasePayment;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IndividualPurchasePaymentRepository extends JpaRepository<IndividualPurchasePayment, Long> {

  @Query("select ipp from IndividualPurchasePayment ipp "
      + "join IndividualPurchase ip on ipp.individualPurchase = ip "
      + "join Purchase p on ip.purchase = p "
      + "join Meeting m on p.meeting = m "
      + "where p.user = :participant and m = :meeting "
      + "order by ipp.createdAt desc ")
  Page<IndividualPurchasePayment> findAllByUserAndMeeting
      (@Param("participant") User participant, @Param("meeting") Meeting meeting, Pageable pageable);

  @Query("select ipp from IndividualPurchasePayment ipp "
      + "join IndividualPurchase ip on ipp.individualPurchase = ip "
      + "join Purchase p on ip.purchase = p "
      + "join Meeting m on p.meeting = m "
      + "where m = :meeting "
      + "order by ipp.createdAt desc ")
  Page<IndividualPurchasePayment> findAllByUserAndMeeting
      (@Param("meeting") Meeting meeting, Pageable pageable);

}
