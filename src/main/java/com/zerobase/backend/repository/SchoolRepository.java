package com.zerobase.backend.repository;

import com.zerobase.backend.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
}
