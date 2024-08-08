package com.zerobase.babdeusilbun.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {

  READY("브라우저 창 이탈, 가상계좌 발급 완료 등 미결제 상태"),
  PAID("결제 완료"),
  FAILED("신용카드 한도 초과, 체크카드 잔액 부족, 브라우저 창 종료 또는 취소 버튼 클릭 등 결제 실패 상태");

  private final String description;

  PaymentStatus(String description) {
    this.description = description;
  }
}
