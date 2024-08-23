package com.zerobase.babdeusilbun.repository.custom.impl;

import static com.zerobase.babdeusilbun.domain.QCategory.*;
import static com.zerobase.babdeusilbun.domain.QMenu.*;
import static com.zerobase.babdeusilbun.domain.QSchool.*;
import static com.zerobase.babdeusilbun.domain.QStoreCategory.*;
import static com.zerobase.babdeusilbun.domain.QStoreSchool.*;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.enums.MeetingStoreSortCriteria;
import static com.querydsl.core.types.ExpressionUtils.count;

import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.QMeeting;
import com.zerobase.babdeusilbun.domain.QStore;
import com.zerobase.babdeusilbun.domain.QStoreImage;
import com.zerobase.babdeusilbun.dto.QStoreDto_SimpleInformation;
import com.zerobase.babdeusilbun.dto.StoreDto.SimpleInformation;
import com.zerobase.babdeusilbun.enums.MeetingStatus;
import com.zerobase.babdeusilbun.repository.custom.CustomStoreRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@Repository
@RequiredArgsConstructor
public class CustomStoreRepositoryImpl implements CustomStoreRepository {
  private final JPAQueryFactory queryFactory;

  private final QStore store = QStore.store;
  private final QStoreImage storeImage = QStoreImage.storeImage;
  private final QMeeting meeting = QMeeting.meeting;

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
        .where(store.deletedAt.isNull())
        .where(menu.deletedAt.isNull())
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
        .where(store.deletedAt.isNull())
        .where(menu.deletedAt.isNull())
        .where(school.id.eq(schoolId))
        .where(where(categoryList, searchMenu))
        .fetchOne();


    return new PageImpl<>(storeList, pageable, count);
  }

  @Override
  public Page<SimpleInformation> getStorePageByEntrepreneur(
      Entrepreneur entrepreneur, Pageable pageable, boolean unprocessedOnly) {
    Long count = getStoresCountByEntrepreneur(entrepreneur, unprocessedOnly);

    return new PageImpl<>(getStoreListByEntrepreneur(
        entrepreneur, pageable, unprocessedOnly), pageable, count);
  }

  private List<SimpleInformation> getStoreListByEntrepreneur(
      Entrepreneur entrepreneur, Pageable pageable, boolean unprocessedOnly) {
    List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable.getSort());

    return queryFactory
        .select(new QStoreDto_SimpleInformation(
            store.id,
            store.name,
            storeImage.url,
            count(meeting.id)
        ))
        .from(store)
        .leftJoin(storeImage).on(storeImage.store.eq(store).and(storeImage.isRepresentative.isTrue()))
        .leftJoin(meeting).on(meeting.store.eq(store).and(meeting.status.eq(MeetingStatus.PURCHASE_COMPLETED)))
        .groupBy(store.id)
        .where(store.entrepreneur.eq(entrepreneur),
            store.deletedAt.isNull())
        .having(unprocessedOnly ? meeting.id.count().gt(0) : null)
        .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
  }

  @Override
  public Long getStoresCountByEntrepreneur(Entrepreneur entrepreneur, boolean unprocessedOnly) {
    return queryFactory
        .select(store.id.count())
        .from(store)
        .leftJoin(storeImage).on(storeImage.store.eq(store).and(storeImage.isRepresentative.isTrue()))
        .leftJoin(meeting).on(meeting.store.eq(store).and(meeting.status.eq(MeetingStatus.PURCHASE_COMPLETED)))
        .groupBy(store.id)
        .where(store.entrepreneur.eq(entrepreneur),
            store.deletedAt.isNull())
        .having(unprocessedOnly ? meeting.id.count().gt(0) : null)
        .fetchOne();
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

    switch (MeetingStoreSortCriteria.valueOf(sortCriteria)) {
      case DELIVERY_TIME -> list.add(orderByDeliveryTime());
      case DELIVERY_FEE -> list.add(orderByDeliveryFee());
      case MIN_PRICE -> list.add(orderByMinPrice());
    }

    return list.toArray(new OrderSpecifier[0]);
  }

  private List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort) {
    List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

    for (Order order : sort) {
      OrderSpecifier<?> orderSpecifier = switch (order.getProperty()) {
        case "name" -> order.isAscending() ?
            store.name.asc() : store.name.desc();
        case "storeId" -> order.isAscending() ?
            store.id.asc() : store.id.desc();
        case "createdAt" -> order.isAscending() ?
            store.createdAt.asc() : store.createdAt.desc();
        case "updatedAt" -> order.isAscending() ?
            store.updatedAt.asc() : store.updatedAt.desc();
        default -> throw new IllegalArgumentException("Unknown sort property: " + order.getProperty());
      };
      orderSpecifiers.add(orderSpecifier);
    }
    return orderSpecifiers;
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
