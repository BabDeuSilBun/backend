package com.zerobase.babdeusilbun.swagger.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface EntrepreneurSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "회원 정보 조회", description = "(로그인한 사업가 정보 조회)")
  @Parameters(value = {
      @Parameter(name = "entrepreneur"),
  })
  @Tag(name = "Entrepreneur")
  @interface GetProfileSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "회원 정보 수정", description = "(로그인한 사업가 정보 수정)")
  @Parameters(value = {
      @Parameter(name = "entrepreneur"),
      @Parameter(name = "request")
  })
  @Tag(name = "Entrepreneur")
  @interface UpdateProfileSwagger {}

}
