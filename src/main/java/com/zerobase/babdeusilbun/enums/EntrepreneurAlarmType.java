package com.zerobase.babdeusilbun.enums;

import lombok.Getter;

@Getter
public enum EntrepreneurAlarmType {

  ORDER_RECEIVED("주문 접수"),
  RIDER_DISPATCH_COMPLETED("라이더 배차 완료");

  private final String description;

  EntrepreneurAlarmType(String description) {
    this.description = description;
  }
}
