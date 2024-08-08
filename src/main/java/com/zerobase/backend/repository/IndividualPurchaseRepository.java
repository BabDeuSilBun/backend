package com.zerobase.backend.repository;

import com.zerobase.backend.domain.IndividualPurchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndividualPurchaseRepository extends JpaRepository<IndividualPurchase, Long> {
}
