package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Address;
import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.Point;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.repository.custom.CustomStoreRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long>, CustomStoreRepository {
  boolean existsByEntrepreneurAndNameAndAddressAndDeletedAtIsNull(
      Entrepreneur entrepreneur, String name, Address address);

  Optional<Store> findByIdAndDeletedAtIsNull(Long storeId);

  Optional<Store> findByIdAndEntrepreneurAndDeletedAtIsNull(Long storeId, Entrepreneur entrepreneur);

  @Query("select s from Store s "
      + "join Meeting m on m.store = s "
      + "join Purchase p on p.meeting = m "
      + "join PurchasePayment pp on pp.purchase = p "
      + "join Point po on po.purchasePayment = pp "
      + "where po = :point ")
  Optional<Store> findStoreByPoint(Point point);
}
