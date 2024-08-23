package com.zerobase.babdeusilbun.enums;

import lombok.Getter;

@Getter
public enum PointSortCriteria {

  EARN("earn", "적립"),
  USE("use", "사용");

  private final String parameter;
  private final String description;

  PointSortCriteria(String parameter, String description) {
    this.parameter = parameter;
    this.description = description;
  }
}
