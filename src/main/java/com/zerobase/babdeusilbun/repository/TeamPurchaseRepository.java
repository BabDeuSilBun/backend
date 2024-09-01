package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.*;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamPurchaseRepository extends JpaRepository<TeamPurchase, Long> {

  @EntityGraph(attributePaths = {"menu", "meeting"})
  Page<TeamPurchase> findAllByMeeting(Meeting meeting, Pageable pageable);

  @EntityGraph(attributePaths = {"menu", "meeting"})
  List<TeamPurchase> findAllByMeeting(Meeting meeting);

  Long countByMeeting(Meeting meeting);

  boolean existsAllByMenuAndMeeting(Menu menu, Meeting meeting);

  Optional<TeamPurchase> findAllById(Long teamPurchaseId);

  @Query("select sum(tp.paymentPrice) "
      + "from TeamPurchase tp "
      + "join Meeting m on tp.meeting = m "
      + "where m = :meeting")
  Long getMeetingTotalPrice(Meeting meeting);

}
