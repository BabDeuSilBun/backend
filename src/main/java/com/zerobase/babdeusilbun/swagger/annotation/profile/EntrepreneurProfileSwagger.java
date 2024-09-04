package com.zerobase.babdeusilbun.swagger.annotation.profile;

import com.zerobase.babdeusilbun.dto.EntrepreneurDto.MyPage;
import com.zerobase.babdeusilbun.exception.ErrorResponse;
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
          content = @Content(schema = @Schema(implementation = MyPage.class))),
      @ApiResponse(
          responseCode = "404", description = "로그인한 사업가의 프로필 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
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
          responseCode = "206", description = "회원 정보 수정에 부분적으로 성공한 경우(이미지 수정이 있는데 적용되지 않은 경우)"),
      @ApiResponse(
          responseCode = "404", description = "로그인한 사업가 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Profile Api")
  @interface UpdateProfileSwagger {}
}
