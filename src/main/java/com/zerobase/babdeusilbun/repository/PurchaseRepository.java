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
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
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

  boolean existsByMeetingAndUser(Meeting meeting, User user);

  @Query("select count(p) "
        + "from Purchase p "
        + "where p.status <> 'CANCEL' and p.meeting = :meeting")
  Long countParticipantByMeeting(Meeting meeting);

  @EntityGraph(attributePaths = {"meeting"})
  Optional<Purchase> findById(Long id);

  @Modifying(clearAutomatically = true)
  @Query(value = "update Purchase p " +
          "set p.status = PurchaseStatus.CANCEL " +
          "WHERE p.meeting != :meeting and p.user = :user and p.status = PurchaseStatus.PRE_PURCHASE")
  void updateUserPreviousMeetingPurchaseStatusFromprepurchaseToCancel(@Param("meeting") Meeting meeting, @Param("user") User user);

  Optional<Purchase> findByMeetingAndUserAndStatusIsNot(Meeting meeting, User user, PurchaseStatus purchaseStatus);
}
