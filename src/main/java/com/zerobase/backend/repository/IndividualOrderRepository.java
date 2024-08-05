package com.zerobase.backend.repository;

import com.zerobase.backend.domain.IndividualOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndividualOrderRepository extends JpaRepository<IndividualOrder, Long> {
}
