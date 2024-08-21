package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.IndividualPurchasePayment;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IndividualPurchasePaymentRepository extends JpaRepository<IndividualPurchasePayment, Long> {

  @Query("select ipp from IndividualPurchasePayment ipp "
      + "join IndividualPurchase ip on ipp.individualPurchase = ip "
      + "join Purchase p on ip.purchase = p "
      + "join Meeting m on p.meeting = m "
      + "where p.user = :participant and m = :meeting ")
  Page<IndividualPurchasePayment> findAllByUserAndMeeting
      (User participant, Meeting meeting, Pageable pageable);

}
