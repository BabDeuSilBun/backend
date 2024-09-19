package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.StoreSchool;
import com.zerobase.babdeusilbun.dto.StoreSchoolDto.Information;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreSchoolRepository extends JpaRepository<StoreSchool, Long> {
  @Query("SELECT ss.school.id FROM StoreSchool ss WHERE ss.store = :store")
  List<Long> findSchoolIdsByStore(@Param("store") Store store);

  int deleteByStoreAndSchool_IdIn(Store store, Set<Long> schoolIds);

  void deleteByStoreAndSchool_IdNotIn(Store store, Set<Long> schoolIds);

  int countByStore(Store store);
  Page<Information> findByStore(Store store, Pageable pageable);
}
