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

public @interface EmailSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "이메일 유효성 검증 이메일 전송")
  @Parameters(value = {
      @Parameter(name = "request"),
  })
  @Tag(name = "Email")
  @interface SendEmailVerifyCodeSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "이메일 유효성 검증 코드 검증")
  @Parameters(value = {
      @Parameter(name = "request"),
  })
  @Tag(name = "Email")
  @interface ConfirmEmailVerifyCodeSwagger {}

}
