package com.zerobase.backend.repository;

import com.zerobase.backend.domain.UserAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAlarmRepository extends JpaRepository<UserAlarm, Long> {
}
