package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.*;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamPurchaseRepository extends JpaRepository<TeamPurchase, Long> {

  @EntityGraph(attributePaths = {"menu", "meeting"})
  Page<TeamPurchase> findAllByMeeting(Meeting meeting, Pageable pageable);

  Long countByMeeting(Meeting meeting);

  boolean existsAllByMenuAndMeeting(Menu menu, Meeting meeting);

  Optional<TeamPurchase> findAllById(Long teamPurchaseId);
}
