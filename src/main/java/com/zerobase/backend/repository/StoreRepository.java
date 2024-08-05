package com.zerobase.backend.repository;

import com.zerobase.backend.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
