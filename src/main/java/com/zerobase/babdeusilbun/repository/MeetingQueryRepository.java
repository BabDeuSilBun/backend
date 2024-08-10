package com.zerobase.babdeusilbun.repository;

import static com.zerobase.babdeusilbun.domain.QMeeting.*;
import static com.zerobase.babdeusilbun.domain.QStore.*;
import static com.zerobase.babdeusilbun.domain.QStoreSchool.*;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.meeting.enums.MeetingSortCriteria;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MeetingQueryRepository {

  private final JPAQueryFactory queryFactory;

  public Page<Meeting> findFilteredMeetingList(Long schoolId, String sortParameter, Pageable pageable) {

    List<Meeting> meetingList = queryFactory.selectFrom(meeting)
        .join(meeting.store, store)
        .join(storeSchool)
        .on(storeSchool.store.eq(store))
        .where(storeSchool.school.id.eq(schoolId))
        .orderBy(getOrderSpecifier(sortParameter))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    return new PageImpl<>(meetingList, pageable, meetingList.size());
  }

  private OrderSpecifier<?>[] getOrderSpecifier(String sortParameter) {

    List<OrderSpecifier<?>> list = new ArrayList<>();

    switch (MeetingSortCriteria.fromParameter(sortParameter)) {
      case DEADLINE -> list.add(orderDeadline());
      case DELIVERY_TIME -> list.add(orderDeliveryTime());
      case DELIVERY_FEE -> list.add(orderDeliveryFee());
      case MIN_PRICE -> list.add(minOrderPrice());
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
  private OrderSpecifier<?> minOrderPrice() {
    return meeting.store.minOrderAmount.asc();
  }

}
