package com.zerobase.backend.enums;

import lombok.Getter;

@Getter
public enum EvaluateBadgeType {

  POSITIVE("긍정 평가 뱃지"),
  NEGATIVE("부정 평가 뱃지");

  private final String description;

  EvaluateBadgeType(String description) {
    this.description = description;
  }


}
