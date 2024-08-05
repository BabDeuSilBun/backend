package com.zerobase.backend.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

  ORDER_IN_PROGRESS("Order In Progress"),
  PAYMENT_REQUESTED("Payment Requested"),
  PAYMENT_FAILED("Payment Failed"),
  PAYMENT_COMPLETED("Payment Completed");

  private final String description;

  OrderStatus(String description) {
    this.description = description;
  }
}
