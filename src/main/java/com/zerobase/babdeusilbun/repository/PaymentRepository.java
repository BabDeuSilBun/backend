package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Payment;
import com.zerobase.babdeusilbun.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

  @Query("select pm from Payment pm "
      + "join Purchase p on pm.purchase = p "
      + "join Meeting m on p.meeting = m "
      + "where m = :meeting and p.user = :participant "
      + "order by pm.createdAt desc ")
  Optional<Payment> findByMeetingAndUser(@Param("meeting") Meeting meeting, @Param("participant") User participant);
}
