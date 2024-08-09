package com.zerobase.babdeusilbun.meeting.enums;

import static com.zerobase.babdeusilbun.exception.ErrorCode.*;

import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum MeetingSortCriteria {

  DEADLINE("deadline", "결제 마감 시간 순"),
  DELIVERY_TIME("shipping-time", "배송시간 순"),
  DELIVERY_FEE("shipping-fee", "배달비 순"),
  MIN_PRICE("min-price", "최소주문금액 순");

  private final String parameter;
  private final String description;

  MeetingSortCriteria(String parameter, String description) {
    this.parameter = parameter;
    this.description = description;
  }

  public static MeetingSortCriteria fromParameter(String parameter) {
    return Arrays.stream(MeetingSortCriteria.values())
        .filter(p -> p.getParameter().equals(parameter)).findFirst()
        .orElseThrow(() -> new CustomException(PARAMETER_INVALID));
  }
}
