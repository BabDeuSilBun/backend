package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.StoreCategory;
import com.zerobase.babdeusilbun.dto.StoreCategoryDto;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreCategoryRepository extends JpaRepository<StoreCategory, Long> {
  @Query("SELECT sc.category.id FROM StoreCategory sc WHERE sc.store = :store")
  List<Long> findCategoryIdsByStore(@Param("store") Store store);

  int deleteByStoreAndCategory_IdIn(Store store, Set<Long> categoryIds);

  void deleteByStoreAndCategory_IdNotIn(Store store, Set<Long> categoryIds);

  int countByStore(Store store);

  Page<StoreCategoryDto.Information> findByStore(Store store, Pageable pageable);
}
