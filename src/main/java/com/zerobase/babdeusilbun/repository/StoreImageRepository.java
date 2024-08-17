package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.StoreImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreImageRepository extends JpaRepository<StoreImage, Long> {

  List<StoreImage> findAllByStoreOrderBySequenceAsc(Store store);
  int countByStore(Store store);

}
