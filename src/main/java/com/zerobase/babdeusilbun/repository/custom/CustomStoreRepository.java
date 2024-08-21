package com.zerobase.babdeusilbun.repository.custom;

import com.zerobase.babdeusilbun.domain.Store;
import java.util.List;
import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.dto.StoreDto.SimpleInformation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomStoreRepository {
  Page<Store> getAvailStoreList(
      List<Long> categoryList, String searchMenu,
      Long schoolId, String sortCriteria, Pageable pageable);
  Page<SimpleInformation> getStorePageByEntrepreneur(
      Entrepreneur entrepreneur, Pageable pageable, boolean unprocessedOnly);
  Long getStoresCountByEntrepreneur(Entrepreneur entrepreneur, boolean unprocessedOnly);
}
