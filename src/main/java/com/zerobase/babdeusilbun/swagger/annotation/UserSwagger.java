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

public @interface UserSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "내 정보 조회")
  @Parameters(value = {
      @Parameter(name = "user")
  })
  @Tag(name = "User")
  @interface GetMyProfileSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "내 정보 수정")
  @Parameters(value = {
      @Parameter(name = "user")
  })
  @Tag(name = "User")
  @interface UpdateProfileSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "내 정보 수정")
  @Parameters(value = {
      @Parameter(name = "user"),
      @Parameter(name = "updateAddress")
  })
  @Tag(name = "User")
  @interface UpdateAddressSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "내 계좌 수정")
  @Parameters(value = {
      @Parameter(name = "user"),
      @Parameter(name = "updateAccount")
  })
  @Tag(name = "User")
  @interface UpdateAccountSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "프로필 조회")
  @Parameters(value = {
      @Parameter(name = "userId")
  })
  @Tag(name = "User")
  @interface GetMeetingInfoSwagger {}

}
