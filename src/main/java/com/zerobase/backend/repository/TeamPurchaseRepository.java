package com.zerobase.backend.repository;

import com.zerobase.backend.domain.Entrepreneur;
import com.zerobase.backend.domain.TeamPurchase;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamPurchaseRepository extends JpaRepository<TeamPurchase, Long> {

  @Query("select tp "
        + "from team_purchase tp "
        + "join tp.meeting m "
        + "join m.store s "
        + "where s.entrepreneur = :entrepreneur")
  List<TeamPurchase> findAllByEntrepreneur(Entrepreneur entrepreneur);
}
