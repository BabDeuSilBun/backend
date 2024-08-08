package com.zerobase.backend.enums;

import lombok.Getter;

@Getter
public enum EvaluateBadge {

  //TODO
  // 테스트용 데이터
  GOOD(EvaluateBadgeType.POSITIVE, "좋음"),
  BAD(EvaluateBadgeType.NEGATIVE, "나쁨");

  private final EvaluateBadgeType type;
  private final String description;

  EvaluateBadge(EvaluateBadgeType type, String description) {
    this.type = type;
    this.description = description;
  }

  @Getter
  public enum EvaluateBadgeType {

    POSITIVE("긍정 평가 뱃지"),
    NEGATIVE("부정 평가 뱃지");

    private final String description;

    EvaluateBadgeType(String description) {
      this.description = description;
    }
  }

}
