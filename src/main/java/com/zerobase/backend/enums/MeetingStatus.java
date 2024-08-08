package com.zerobase.backend.enums;

import lombok.Getter;


@Getter
public enum MeetingStatus {

  GATHERING("모집중"),
  ORDER_COMPLETED("주문 완료"),
  ORDER_CANCELLED("주문 취소"),
  COOKING("조리중"),
  COOKING_COMPLETED("조리 완료"),
  IN_DELIVERY("배송중"),
  DELIVERY_COMPLETED("배송 완료"),
  MEETING_CANCELLED("모임 취소"),
  MEETING_COMPLETED("모임 완료");

  private final String description;

  MeetingStatus(String description) {
    this.description = description;
  }

}
