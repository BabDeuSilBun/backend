package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Purchase;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.enums.PurchaseStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.ModelAttribute;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

  @Query("select p "
      +   "from Purchase p "
      +   "join p.meeting m "
      +   "join m.store s "
      +   "where s.entrepreneur = :entrepreneur "
      +   "and m.status != 'MEETING_CANCELLED' and m.status != 'MEETING_COMPLETED' ")
  List<Purchase> findProceedingByOwner(Entrepreneur entrepreneur);

  List<Purchase> findAllByMeeting(Meeting meeting);

  @EntityGraph(attributePaths = {"meeting", "user"})
  Optional<Purchase> findByMeetingAndUser(Meeting meeting, User user);

  @Query("select p from Purchase p "
      + "where p.meeting = :meeting and p.status <> 'CANCEL'")
  List<Purchase> findProceedingByMeeting(Meeting meeting);

  boolean existsByUser(User participant);

  boolean existsByMeetingAndUser(Meeting meeting, User user);

}
