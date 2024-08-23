package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Point;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.enums.PointType;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PointRepository extends JpaRepository<Point, Long> {

  @EntityGraph(attributePaths = "purchasePayment")
  Page<Point> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);

  @Query(value = "select p from Point p "
        + "where p.user = :user and p.type in :type "
        + "order by p.createdAt desc ",
      countQuery = "select count(p.id) from Point p "
          + "where p.user = :user and p.type in :type "
          + "order by p.createdAt desc")
  Page<Point> findSortedAllByUser(User user, List<PointType> type, Pageable pageable);
}
