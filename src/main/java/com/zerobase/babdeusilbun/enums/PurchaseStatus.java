package com.zerobase.babdeusilbun.enums;

import lombok.Getter;

@Getter
public enum PurchaseStatus {

  PRE_PURCHASE("주문 전"),
  RECEIVED("주문 접수"),
  PROGRESS("주문 진행중"),
  CANCEL("주문 취소"),
  PAYMENT_REQUESTED("결제 필요"),
  PAYMENT_FAILED("결제 실패"),
  PAYMENT_COMPLETED("결제 완료");


  private final String description;

  PurchaseStatus(String description) {
    this.description = description;
  }

}
