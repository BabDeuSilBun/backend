package com.zerobase.babdeusilbun.repository.custom.impl;

import static com.zerobase.babdeusilbun.domain.QCategory.*;
import static com.zerobase.babdeusilbun.domain.QMenu.*;
import static com.zerobase.babdeusilbun.domain.QSchool.*;
import static com.zerobase.babdeusilbun.domain.QStore.*;
import static com.zerobase.babdeusilbun.domain.QStoreCategory.*;
import static com.zerobase.babdeusilbun.domain.QStoreSchool.*;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.enums.SortCriteria;
import com.zerobase.babdeusilbun.repository.custom.CustomStoreRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class CustomStoreRepositoryImpl implements CustomStoreRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Store> getAvailStoreList(List<Long> categoryList, String searchMenu, Long schoolId,
      String sortCriteria, Pageable pageable) {

    List<Store> storeList = queryFactory.selectFrom(store)
        .join(storeCategory).on(storeCategory.store.eq(store))
        .join(category).on(storeCategory.category.eq(category))
        .join(storeSchool).on(storeSchool.store.eq(store))
        .join(school).on(storeSchool.school.eq(school))
        .join(menu).on(menu.store.eq(store))
        .where(school.id.eq(schoolId))
        .where(where(categoryList, searchMenu))
        .orderBy(getOrderSpecifier(sortCriteria))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long count = queryFactory
        .select(store.count()).from(store)
        .join(storeCategory).on(storeCategory.store.eq(store))
        .join(category).on(storeCategory.category.eq(category))
        .join(storeSchool).on(storeSchool.store.eq(store))
        .join(school).on(storeSchool.school.eq(school))
        .join(menu).on(menu.store.eq(store))
        .where(school.id.eq(schoolId))
        .where(where(categoryList, searchMenu))
        .fetchOne();


    return new PageImpl<>(storeList, pageable, count);
  }

  private BooleanExpression[] where(List<Long> categoryList, String searchMenu) {
    List<BooleanExpression> list = new ArrayList<>();

    if (!categoryList.isEmpty()) {
      list.add(filterCategory(categoryList));
    }

    if (StringUtils.hasText(searchMenu)) {
      list.add(searchMenuName(searchMenu));
    }

    return list.toArray(new BooleanExpression[0]);
  }

  private OrderSpecifier<?>[] getOrderSpecifier(String sortCriteria) {
    List<OrderSpecifier<?>> list = new ArrayList<>();

    switch (SortCriteria.valueOf(sortCriteria)) {
      case DELIVERY_TIME -> list.add(orderByDeliveryTime());
      case DELIVERY_FEE -> list.add(orderByDeliveryFee());
      case MIN_PRICE -> list.add(orderByMinPrice());
    }

    return list.toArray(new OrderSpecifier[0]);
  }

  private BooleanExpression filterCategory(List<Long> categoryList) {
    return category.id.in(categoryList);
  }

  private BooleanExpression searchMenuName(String searchMenu) {
    return menu.name.like(searchMenu);
  }

  private OrderSpecifier<?> orderByDeliveryTime() {
    return store.minDeliveryTime.asc();
  }

  private OrderSpecifier<?> orderByDeliveryFee() {
    return store.deliveryPrice.asc();
  }

  private OrderSpecifier<?> orderByMinPrice() {
    return store.minPurchaseAmount.asc();
  }
}
