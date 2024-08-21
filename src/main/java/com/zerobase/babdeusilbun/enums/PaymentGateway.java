package com.zerobase.babdeusilbun.enums;

import lombok.Getter;

@Getter
public enum PaymentGateway {

  KAKAOPAY("카카오페이");

  private final String description;

  PaymentGateway(String description) {
    this.description = description;
  }
}
