package com.zerobase.babdeusilbun.repository.custom;

import com.zerobase.babdeusilbun.domain.Store;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomStoreRepository {

  Page<Store> getAvailStoreList(
      List<Long> categoryList, String searchMenu,
      Long schoolId, String sortCriteria, Pageable pageable);

}
