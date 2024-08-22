package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.StoreImage;
import com.zerobase.babdeusilbun.dto.StoreImageDto;
import com.zerobase.babdeusilbun.dto.StoreImageDto.Thumbnail;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreImageRepository extends JpaRepository<StoreImage, Long> {
  List<StoreImage> findAllByStoreOrderBySequenceAsc(Store store);
  int countByStore(Store store);
  Page<StoreImageDto.Information> findByStore(Store store, Pageable pageable);
  Optional<Thumbnail> findFirstByStoreAndIsRepresentativeTrue(Store store);
}
