package com.zerobase.backend.enums;

import lombok.Getter;

@Getter
public enum PurchaseStatus {

  ORDER_IN_PROGRESS("Purchase In Progress"),
  PAYMENT_REQUESTED("Payment Requested"),
  PAYMENT_FAILED("Payment Failed"),
  PAYMENT_COMPLETED("Payment Completed");

  private final String description;

  PurchaseStatus(String description) {
    this.description = description;
  }
}
