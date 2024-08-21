package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Point;
import com.zerobase.babdeusilbun.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {

  @EntityGraph(attributePaths = "purchasePayment")
  Page<Point> findAllByUser(User user, Pageable pageable);
}
