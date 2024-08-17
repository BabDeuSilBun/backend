package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Address;
import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.Store;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
  boolean existsByEntrepreneurAndNameAndAddressAndDeletedAtIsNull(
      Entrepreneur entrepreneur, String name, Address address);

  Optional<Store> findByIdAndDeletedAtIsNull(Long storeId);
}
