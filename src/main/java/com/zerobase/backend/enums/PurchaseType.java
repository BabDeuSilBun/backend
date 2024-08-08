package com.zerobase.backend.enums;

public enum PurchaseType {

  DINING_TOGETHER("함께 식사"),
  DELIVERY_TOGETHER("함께 배송");

  private final String description;

  PurchaseType(String description) {
    this.description = description;
  }
}
