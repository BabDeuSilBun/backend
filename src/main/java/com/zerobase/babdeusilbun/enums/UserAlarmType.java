package com.zerobase.babdeusilbun.enums;

import lombok.Getter;

@Getter
public enum UserAlarmType {

  MEETING_CONDITION_MET("모임 조건 충족", "%s에 대해 주문 가능한 조건이 모두 충족되었어요!"),
  ORDER_COMPLETED("주문 완료", "%s에 대한 주문을 완료했어요!"),
  ORDER_APPROVED("주문 승인", "%s에 대한 주문이 승인되었어요! 조리를 진행해요."),
  ORDER_REJECTED("주문 거절", "%s에 대한 주문이 거절되었어요."),
  COOKING_COMPLETED("조리 완료", "%s에 대한 요리가 완료되었어요!"),
  DELIVERY_STARTED("배송 시작", "%s에 대한 배송이 시작되었어요!"),
  DELIVERY_COMPLETED("배송 완료", "%s에 대한 배송이 완료되었어요!"),
  ORDER_DELAY("주문 지연", "%s에 대한 주문이 지연되고 있어요.\n지연 사유: %s"),
  REVIEW_RECEIVED("평가 수신", "%s에 대해 모임원이 나를 평가했어요!"),
  POINT_REFUND("포인트 환급", "%s에 대해 %s의 포인트를 환급받았어요."),
  POINT_WITHDRAWAL("포인트 인출", "%s의 포인트를 인출했어요.");

  private final String description;
  private final String message;

  UserAlarmType(String description, String message) {
    this.description = description;
    this.message = message;
  }

  public String getMessage(String string) {
    return String.format(message, string);
  }

  public String getMessage(String string1, String string2) {
    return String.format(message, string1, string2);
  }
}
