package com.zerobase.backend.repository;

import com.zerobase.backend.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
}
