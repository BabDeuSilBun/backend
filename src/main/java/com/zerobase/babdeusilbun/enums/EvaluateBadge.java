package com.zerobase.babdeusilbun.enums;

import lombok.Getter;

@Getter
public enum EvaluateBadge {

  GOOD_COMMUNICATION(EvaluateBadgeType.POSITIVE, "소통이 잘 돼요"),
  GOOD_TIMECHECK(EvaluateBadgeType.POSITIVE, "시간 약속을 잘 지켜요"),
  GOOD_TOGETHER(EvaluateBadgeType.POSITIVE, "같이 먹기 즐거워요"),
  GOOD_RESPONSE(EvaluateBadgeType.POSITIVE, "응답이 빨라요"),

  BAD_RESPONSE(EvaluateBadgeType.NEGATIVE, "연락을 잘 안받아요"),
  BAD_TIMECHECK(EvaluateBadgeType.NEGATIVE, "시간 약속을 안 지켜요"),
  BAD_TOGETHER(EvaluateBadgeType.NEGATIVE, "같이 먹기 불편해요");

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
