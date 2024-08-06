package com.zerobase.backend.repository;

import com.zerobase.backend.domain.Entrepreneur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntrepreneurRepository extends JpaRepository<Entrepreneur, Long> {

  boolean existsByEmail(String email);
}
