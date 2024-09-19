package com.zerobase.babdeusilbun.enums;

import lombok.Getter;

@Getter
public enum PurchaseType {

  DINING_TOGETHER("함께 식사") , // 공동 주문
  DELIVERY_TOGETHER("함께 배송");// 개인 주문

  private final String description;

  PurchaseType(String description) {
    this.description = description;
  }
}
