package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Menu;
import com.zerobase.babdeusilbun.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    boolean existsByStoreAndNameAndPriceAndDeletedAtIsNull(Store store, String name, long price);
}
