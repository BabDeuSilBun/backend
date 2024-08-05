package com.zerobase.backend.enums;

import lombok.Getter;

@Getter
public enum UserAlarmType {

  MEETING_CONDITION_MET("모임 조건 충족"),
  ORDER_COMPLETED("주문 완료"),
  ORDER_APPROVED("주문 승인"),
  ORDER_REJECTED("주문 거절"),
  COOKING_COMPLETED("조리 완료"),
  DELIVERY_STARTED("배송 시작"),
  DELIVERY_COMPLETED("배송 완료"),
  REVIEW_RECEIVED("평가 수신");

  private final String description;

  UserAlarmType(String description) {
    this.description = description;
  }
}
