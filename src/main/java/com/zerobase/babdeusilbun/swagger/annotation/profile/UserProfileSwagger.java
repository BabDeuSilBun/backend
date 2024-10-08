package com.zerobase.babdeusilbun.swagger.annotation.profile;

import com.zerobase.babdeusilbun.dto.EvaluateDto.MyEvaluates;
import com.zerobase.babdeusilbun.dto.UserDto.MyPage;
import com.zerobase.babdeusilbun.dto.UserDto.UpdateAccount;
import com.zerobase.babdeusilbun.dto.UserDto.UpdateAddress;
import com.zerobase.babdeusilbun.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface UserProfileSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "프로필 조회 api",
      description = "로그인한 일반 이용자 권한의 이용자 정보 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "프로필 조회에 성공한 경우",
          content = @Content(schema = @Schema(implementation = MyPage.class))),
      @ApiResponse(
          responseCode = "404", description = "로그인한 이용자 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Profile Api")
  @interface GetMyProfileSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "회원 정보 수정 api",
      description = "로그인한 일반 이용자 권한의 이용자 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "회원 정보 수정에 성공한 경우"),
      @ApiResponse(
          responseCode = "206", description = "회원 정보 수정에 부분적으로 성공한 경우(이미지, 학교, 학과 변경을 요청했는데 제외된 항목이 있는 경우)"),
      @ApiResponse(
          responseCode = "404", description = "로그인한 회원 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Profile Api")
  @interface UpdateProfileSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "내 주소 정보 수정 api",
      description = "로그인한 이용자의 주소 정보 수정")
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = UpdateAddress.class)),
      description = "주소 정보 수정사항")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "주소 정보 수정에 성공한 경우"),
      @ApiResponse(
          responseCode = "404", description = "로그인한 회원 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Profile Api")
  @interface UpdateAddressSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "내 계좌 정보 수정 api",
      description = "로그인한 이용자의 계좌 정보 수정")
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = UpdateAccount.class)),
      description = "계좌 정보 수정사항")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "계좌 정보 수정에 성공한 경우"),
      @ApiResponse(
          responseCode = "404", description = "로그인한 회원 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Profile Api")
  @interface UpdateAccountSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "받은 평가 내역 조회 api",
      description = "모임에 참여하며 다른 사람들에게서 받았던 평가 내역 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "받은 평가 내역 조회에 성공한 경우",
          content = @Content(schema = @Schema(implementation = MyEvaluates.class)))
  })
  @Tag(name = "User Profile Api")
  @interface GetMyEvaluatesSwagger {}
}
