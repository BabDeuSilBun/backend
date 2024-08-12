package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Purchase;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.enums.PurchaseStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

  @Query("select p "
      +   "from purchase p "
      +   "join p.meeting m "
      +   "join m.store s "
      +   "where s.entrepreneur = :entrepreneur "
      +   "and m.status != 'MEETING_CANCELLED' and m.status != 'MEETING_COMPLETED' ")
  List<Purchase> findProceedingByOwner(Entrepreneur entrepreneur);

  List<Purchase> findAllByMeeting(Meeting meeting);

  Optional<Purchase> findByMeetingAndUser(Meeting meeting, User user);

  @Query("select p from purchase p "
      + "where p.meeting = :meeting and p.status <> 'CANCEL'")
  List<Purchase> findProceedingByMeeting(Meeting meeting);

}
