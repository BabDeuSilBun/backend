package com.zerobase.babdeusilbun.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum Bank {

  SHINHAN("신한"),
  NH("농협"),
  HANA("하나"),
  KOOKMIN("국민"),
  KAKAO("카카오뱅크"),
  TOSS("토스");

  private final String description;

  Bank(String description) {
    this.description = description;
  }

  @JsonCreator
  public static Bank from(String s) {
    return Bank.valueOf(s.toUpperCase());
  }
}
