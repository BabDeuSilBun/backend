package com.zerobase.backend.enums;

import lombok.Getter;

@Getter
public enum InquiryStatus {

  PENDING("답변 대기"),
  COMPLETED("답변 완료");

  private final String description;

  InquiryStatus(String description) {
    this.description = description;
  }
}
