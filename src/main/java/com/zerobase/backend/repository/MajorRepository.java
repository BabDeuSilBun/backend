package com.zerobase.backend.repository;

import com.zerobase.backend.domain.Major;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorRepository extends JpaRepository<Major, Long> {
}
