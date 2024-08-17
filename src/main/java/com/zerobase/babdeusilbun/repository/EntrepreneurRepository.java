package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Entrepreneur;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntrepreneurRepository extends JpaRepository<Entrepreneur, Long> {

  boolean existsByEmail(String email);

  Optional<Entrepreneur> findByEmail(String email);

  Optional<Entrepreneur> findByIdAndDeletedAtIsNull(Long entrepreneurId);
}
