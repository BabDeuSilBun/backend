package com.zerobase.babdeusilbun.enums;

import lombok.Getter;

@Getter
public enum PurchaseStatus {

  ORDER_RECEIVED("주문 접수"),
  ORDER_IN_PROGRESS("주문 진행중"),
  PAYMENT_REQUESTED("결제 필요"),
  PAYMENT_FAILED("결제 실패"),
  PAYMENT_COMPLETED("결제 완료");


  private final String description;

  PurchaseStatus(String description) {
    this.description = description;
  }

}
