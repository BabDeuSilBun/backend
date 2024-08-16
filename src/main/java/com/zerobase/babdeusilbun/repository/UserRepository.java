package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

  Optional<User> findByIdAndDeletedAtIsNotNull(Long userId);
}
