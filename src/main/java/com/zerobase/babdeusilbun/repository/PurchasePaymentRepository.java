package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.PurchasePayment;
import com.zerobase.babdeusilbun.domain.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PurchasePaymentRepository extends JpaRepository<PurchasePayment, Long> {

  @Query("select pp from PurchasePayment pp "
      + "join Purchase p on pp.purchase = p "
      + "join Meeting m on p.meeting = m "
      + "where p.user = :participant and m = :meeting "
      + "order by pp.createdAt desc ")
  Optional<PurchasePayment> findByMeetingAndUser(@Param("meeting") Meeting meeting, User participant);

}
