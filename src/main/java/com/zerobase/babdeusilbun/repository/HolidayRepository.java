package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Holiday;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.dto.HolidayDto;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
  @Query("SELECT h.dayOfWeek FROM Holiday h WHERE h.store = :store")
  List<DayOfWeek> findHolidaysByStore(@Param("store") Store store);

  int deleteByStoreAndDayOfWeekIn(Store store, Set<DayOfWeek> dayOfWeeks);

  int countByStore(Store store);

  @Query("SELECT h FROM Holiday h WHERE h.store = :store " +
      "ORDER BY CASE h.dayOfWeek " +
      "WHEN 'MONDAY' THEN 1 " +
      "WHEN 'TUESDAY' THEN 2 " +
      "WHEN 'WEDNESDAY' THEN 3 " +
      "WHEN 'THURSDAY' THEN 4 " +
      "WHEN 'FRIDAY' THEN 5 " +
      "WHEN 'SATURDAY' THEN 6 " +
      "WHEN 'SUNDAY' THEN 7 " +
      "ELSE 8 END ASC")
  Page<HolidayDto.Information> findByStoreOrderByDayOfWeek(@Param("store") Store store, Pageable pageable);
}
