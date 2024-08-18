package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Holiday;
import com.zerobase.babdeusilbun.domain.Store;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
  @Query("SELECT h.dayOfWeek FROM Holiday h WHERE h.store = :store")
  List<DayOfWeek> findHolidaysByStore(@Param("store") Store store);

  int deleteByStoreAndDayOfWeekIn(Store store, Set<DayOfWeek> dayOfWeeks);
}
