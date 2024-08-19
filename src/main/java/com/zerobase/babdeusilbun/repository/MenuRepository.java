package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Menu;
import com.zerobase.babdeusilbun.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByIdAndDeletedAtIsNull(Long menuId);

    boolean existsByStoreAndNameAndPriceAndDeletedAtIsNull(Store store, String name, long price);
}
