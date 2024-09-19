package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Menu;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.dto.MenuDto.Information;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByIdAndDeletedAtIsNull(Long menuId);

    boolean existsByStoreAndNameAndPriceAndDeletedAtIsNull(Store store, String name, long price);
    int countByStoreAndDeletedAtIsNull(Store store);
    Page<Information> findByStoreAndDeletedAtIsNull(Store store, Pageable pageable);
}
