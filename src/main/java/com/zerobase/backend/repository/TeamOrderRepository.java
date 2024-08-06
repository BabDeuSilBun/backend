package com.zerobase.backend.repository;

import com.zerobase.backend.domain.TeamOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamOrderRepository extends JpaRepository<TeamOrder, Long> {
}
