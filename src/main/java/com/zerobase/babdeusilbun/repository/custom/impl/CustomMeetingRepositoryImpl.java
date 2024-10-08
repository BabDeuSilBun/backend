package com.zerobase.babdeusilbun.repository.custom.impl;

import static com.zerobase.babdeusilbun.domain.QMeeting.meeting;
import static com.zerobase.babdeusilbun.domain.QStore.store;
import static com.zerobase.babdeusilbun.domain.QStoreCategory.storeCategory;
import static com.zerobase.babdeusilbun.domain.QStoreSchool.storeSchool;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.enums.MeetingStoreSortCriteria;
import com.zerobase.babdeusilbun.repository.custom.CustomMeetingRepository;
import java.time.LocalDateTime;
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
public class CustomMeetingRepositoryImpl implements CustomMeetingRepository {

  private final JPAQueryFactory queryFactory;

  public Page<Meeting> findFilteredMeetingList
      (Long schoolId, String sortParameter, String searchMenu, Long categoryFilter, Pageable pageable) {

    List<Meeting> meetingList = queryFactory.selectFrom(meeting)
        .leftJoin(meeting.store, store)
        .fetchJoin()
        .leftJoin(storeSchool).on(storeSchool.store.eq(store))
        .leftJoin(storeCategory).on(storeCategory.store.eq(store))
        .where(where(schoolId, searchMenu, categoryFilter))
        .where(meeting.paymentAvailableDt.after(LocalDateTime.now()))
        .orderBy(getOrderSpecifier(sortParameter))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    return new PageImpl<>(meetingList, pageable, meetingList.size());
  }

  private BooleanExpression[] where(Long schoolId, String searchMenu, Long categoryFilter) {
    List<BooleanExpression> list = new ArrayList<>();
    list.add(schoolExpression(schoolId));

    if (StringUtils.hasText(searchMenu)) {
      list.add(searchMenuExpression(searchMenu));
    }

    if (categoryFilter != null) {
      list.add(categoryExpression(categoryFilter));
    }

    return list.toArray(new BooleanExpression[0]);
  }

  private BooleanExpression categoryExpression(Long categoryId) {
    return storeCategory.category.id.eq(categoryId);
  }

  private BooleanExpression schoolExpression(Long schoolId) {
    return storeSchool.school.id.eq(schoolId);
  }

  // 검색어
  private BooleanExpression searchMenuExpression(String searchMenu) {
    return meeting.store.name.like(searchMenu);
  }

  private OrderSpecifier<?>[] getOrderSpecifier(String sortParameter) {

    List<OrderSpecifier<?>> list = new ArrayList<>();

    switch (MeetingStoreSortCriteria.fromParameter(sortParameter)) {
      case DEADLINE -> list.add(orderDeadline());
      case DELIVERY_TIME -> list.add(orderDeliveryTime());
      case DELIVERY_FEE -> list.add(orderDeliveryFee());
      case MIN_PRICE -> list.add(minPurchasePrice());
    }

    return list.toArray(new OrderSpecifier[0]);
  }

  // 결제시간 마감 임박한 순
  private OrderSpecifier<?> orderDeadline() {
    return meeting.paymentAvailableDt.asc();
  }

  // 배송시간 빠른 순
  private OrderSpecifier<?> orderDeliveryTime() {
    return meeting.deliveredAt.asc();
  }

  // 배송비 저렴한 순
  private OrderSpecifier<?> orderDeliveryFee() {
    return meeting.store.deliveryPrice.asc();
  }

  // 최소 주문 금액 저렴한 순
  private OrderSpecifier<?> minPurchasePrice() {
    return meeting.store.minPurchaseAmount.asc();
  }



}
