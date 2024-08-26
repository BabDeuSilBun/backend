package com.zerobase.babdeusilbun.enums;

import static com.zerobase.babdeusilbun.enums.PointType.*;
import static com.zerobase.babdeusilbun.exception.ErrorCode.*;

import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum PointSortCriteria {

  EARN("earn", PLUS, "적립"),
  USE("use", MINUS, "사용");

  private final String parameter;
  private final PointType pointType;
  private final String description;

  PointSortCriteria(String parameter, PointType pointType, String description) {
    this.parameter = parameter;
    this.pointType = pointType;
    this.description = description;
  }

  public static PointSortCriteria fromParameter(String parameter) {
    if (parameter == null) {
      return null;
    }
    return Arrays.stream(PointSortCriteria.values())
        .filter(p -> p.parameter.equals(parameter)).findFirst()
        .orElseThrow(() -> new CustomException(PARAMETER_INVALID));
  }
}
