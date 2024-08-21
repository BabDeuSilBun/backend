package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Payment;
import com.zerobase.babdeusilbun.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

  @Query("select pm from Payment pm "
      + "join Purchase p on pm.purchase = p "
      + "join Meeting m on p.meeting = m "
      + "where m = :meeting and p.user = :participant ")
  Optional<Payment> findByMeetingAndUser(Meeting meeting, User participant);
}
