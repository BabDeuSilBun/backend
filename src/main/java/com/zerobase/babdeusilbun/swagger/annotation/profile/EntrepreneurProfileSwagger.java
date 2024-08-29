package com.zerobase.babdeusilbun.swagger.annotation.profile;

import com.zerobase.babdeusilbun.dto.EntrepreneurDto.MyPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface EntrepreneurProfileSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "프로필 조회 api",
      description = "로그인한 사업가 권한의 이용자 정보 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "프로필 조회에 성공한 경우",
          content = @Content(schema = @Schema(implementation = MyPage.class)))
  })
  @Tag(name = "Entrepreneur Profile Api")
  @interface GetProfileSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "회원 정보 수정 api",
      description = "로그인한 사업가 권한의 이용자 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "회원 정보 수정에 성공한 경우"),
      @ApiResponse(
          responseCode = "206", description = "회원 정보 수정에 부분적으로 성공한 경우")
  })
  @Tag(name = "Entrepreneur Profile Api")
  @interface UpdateProfileSwagger {}
}
