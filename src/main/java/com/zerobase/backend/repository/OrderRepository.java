package com.zerobase.backend.repository;

import com.zerobase.backend.domain.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Purchase, Long> {
}
