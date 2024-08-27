package com.zerobase.babdeusilbun.enums;

import io.swagger.v3.oas.models.tags.Tag;
import lombok.Getter;

@Getter
public enum SwaggerTag {
  AUTHORIZATION_USER("User Authorization Controller", "일반 이용자 인증 컨트롤러"),
  AUTHORIZATION_ENTREPRENEUR("Business Authorization Controller", "사업가 이용자 인증 컨트롤러"),
  COMMON_SCHOOL("Common School Controller", "공용 학교(캠퍼스) 관련 컨트롤러");

  private final String name;
  private final String description;
  private final Tag tag;

  SwaggerTag (String name, String description) {
    this.name = name;
    this.description = description;
    this.tag = new Tag().name(name).description(description);
  }
}
