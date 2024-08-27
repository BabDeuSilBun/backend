package com.zerobase.babdeusilbun.swagger.enums;

import lombok.Getter;

@Getter
public enum SwaggerTagType {

  SCHOOL("School");

  @Getter
  private final String name;

  SwaggerTagType(String name) {
    this.name = name;
  }
}
