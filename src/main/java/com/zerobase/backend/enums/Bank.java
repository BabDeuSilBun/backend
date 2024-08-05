package com.zerobase.backend.enums;

import lombok.Getter;

@Getter
public enum Bank {
  SHINHAN("신한"),
  NH("농협"),
  HANA("하나"),
  KOOKMIN("국민"),
  KAKAO("카카오뱅크"),
  TOSS("토스");

  /*
  신한, 농협, 하나, 국민, 카카오뱅크, 우리
   */

  private final String korean;

  Bank(String korean) {
    this.korean = korean;
  }
}
