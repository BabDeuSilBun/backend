package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Purchase;
import com.zerobase.babdeusilbun.domain.TeamPurchase;
import com.zerobase.babdeusilbun.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamPurchaseRepository extends JpaRepository<TeamPurchase, Long> {

  @EntityGraph(attributePaths = {"menu", "meeting"})
  Page<TeamPurchase> findAllByMeeting(Meeting meeting, Pageable pageable);

  @EntityGraph(attributePaths = {"menu", "meeting"})
  List<TeamPurchase> findAllByMeeting(Meeting meeting);

  Long countByMeeting(Meeting meeting);

}
