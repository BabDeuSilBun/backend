package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.PurchaseDto.MenuResponse;
import com.zerobase.babdeusilbun.enums.MeetingStatus;
import com.zerobase.babdeusilbun.repository.custom.CustomMeetingRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long>, CustomMeetingRepository {

  @Query("select m from Meeting m "
      + "where m.leader = :leader "
      + "and m.status != 'MEETING_CANCELLED' and m.status != 'MEETING_COMPLETED' ")
  List<Meeting> findProceedingByLeader(@Param("leader") User leader);

  @Query("select m from Meeting m "
      + "join Purchase p on p.meeting = m "
      + "where p.user = :participant "
      + "and m.status != 'MEETING_CANCELLED' and m.status != 'MEETING_COMPLETED' ")
  List<Meeting> findProceedingByParticipant(@Param("participant") User participant);

  @Query("select m from Meeting m where m.id = :id")
  @EntityGraph(attributePaths = "store")
  Optional<Meeting> findWithStoreById(@Param("id") Long id);

  Optional<Meeting> findAllByIdAndDeletedAtIsNull(Long id);

  Optional<Meeting> findByIdAndStatusInAndDeletedAtIsNull(Long meetingId, List<MeetingStatus> statuses);

  int countByStoreAndStatusInAndDeletedAtIsNull(Store store, List<MeetingStatus> statuses);

  @Query("SELECT m FROM Meeting m " +
      "JOIN MeetingPurchaseTime mpt ON m.id = mpt.meeting.id " +
      "WHERE m.store = :store AND m.status IN :statuses AND m.deletedAt IS NULL " +
      "ORDER BY mpt.createdAt DESC")
  Page<Meeting> findAllByStoreAndStatusInAndDeletedAtIsNullOrderByPurchaseTimeCreatedAtDesc(
      Store store, List<MeetingStatus> statuses, Pageable pageable);

  @Query(value = "SELECT COUNT(DISTINCT menu_id) " +
      "FROM (" +
      "    SELECT tp.menu_id " +
      "    FROM team_purchase_payment tp " +
      "    JOIN team_purchase t ON t.id = tp.team_purchase_id " +
      "    WHERE t.meeting_id = :meetingId " +
      "    UNION ALL " +
      "    SELECT ip.menu_id " +
      "    FROM individual_purchase_payment ip " +
      "    JOIN individual_purchase i ON i.id = ip.individual_purchase_id " +
      "    JOIN purchase p ON p.id = i.purchase_id " +
      "    WHERE p.meeting_id = :meetingId AND p.status = :status " +
      ") AS combined",
      nativeQuery = true)
  int countPurchaseMenuByMeetingAndStatus(@Param("meetingId") Long meetingId,
      @Param("status") String status);

  @Query(value = "SELECT menu_id AS menuId, menu_name AS menuName, SUM(quantity) AS quantity " +
      "FROM (" +
      "    SELECT tp.menu_id, tp.menu_name, tp.quantity " +
      "    FROM team_purchase_payment tp " +
      "    JOIN team_purchase t ON t.id = tp.team_purchase_id " +
      "    WHERE t.meeting_id = :meetingId " +
      "    UNION ALL " +
      "    SELECT ip.menu_id, ip.menu_name, ip.quantity " +
      "    FROM individual_purchase_payment ip " +
      "    JOIN individual_purchase i ON i.id = ip.individual_purchase_id " +
      "    JOIN purchase p ON p.id = i.purchase_id " +
      "    WHERE p.meeting_id = :meetingId AND p.status = :status " +
      ") AS combined " +
      "GROUP BY menu_id, menu_name",
      countQuery = "SELECT COUNT(*) FROM ( " +
          "    SELECT tp.menu_id " +
          "    FROM team_purchase_payment tp " +
          "    JOIN team_purchase t ON t.id = tp.team_purchase_id " +
          "    WHERE t.meeting_id = :meetingId " +
          "    UNION ALL " +
          "    SELECT ip.menu_id " +
          "    FROM individual_purchase_payment ip " +
          "    JOIN individual_purchase i ON i.id = ip.individual_purchase_id " +
          "    JOIN purchase p ON p.id = i.purchase_id " +
          "    WHERE p.meeting_id = :meetingId AND p.status = :status " +
          ") AS combined",
      nativeQuery = true)
  Page<MenuResponse> findAllPurchaseMenuByMeetingAndStatus(@Param("meetingId") Long meetingId,
      @Param("status") String status, Pageable pageable);
}
