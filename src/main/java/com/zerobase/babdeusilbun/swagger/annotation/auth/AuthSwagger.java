package com.zerobase.babdeusilbun.swagger.annotation.auth;

import com.zerobase.babdeusilbun.exception.ErrorResponse;
import com.zerobase.babdeusilbun.security.dto.SignRequest;
import com.zerobase.babdeusilbun.security.dto.SignResponse;
import com.zerobase.babdeusilbun.security.dto.WithdrawalRequest;
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

public @interface AuthSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "일반 이용자 로그인 api",
      description = "일반 이용자 권한으로 로그인")
  @RequestBody(
      content = @Content(schema = @Schema(implementation = SignRequest.SignIn.class)),
      description = "이메일과 비밀번호")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "로그인에 성공한 경우",
          content = @Content(schema = @Schema(implementation = SignResponse.class))),
      @ApiResponse(
          responseCode = "404", description = "가입된 이메일이 아닌 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "401", description = "탈퇴한 이용자거나 비밀번호가 틀린 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Auth Api")
  @interface UserSigninSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "사업가 로그인 api",
      description = "사업가 권한으로 로그인")
  @RequestBody(
      content = @Content(schema = @Schema(implementation = SignRequest.SignIn.class)),
      description = "이메일과 비밀번호")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "로그인에 성공한 경우",
          content = @Content(schema = @Schema(implementation = SignResponse.class))),
      @ApiResponse(
          responseCode = "404", description = "가입된 이메일이 아닌 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "401", description = "탈퇴한 이용자거나 비밀번호가 틀린 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Auth Api")
  @interface BusinessSigninSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "로그아웃 api",
      description = "이용자 로그아웃")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "로그아웃에 성공한 경우"),
      @ApiResponse(
          responseCode = "401", description = "토큰이 유효하지 않은 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Auth Api")
  @Tag(name = "Entrepreneur Auth Api")
  @interface LogoutSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "일반 이용자 회원탈퇴 api",
      description = "일반 이용자의 회원탈퇴")
  @RequestBody(
      content = @Content(schema = @Schema(implementation = WithdrawalRequest.class)),
      description = "현재 계정의 비밀번호")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "탈퇴에 성공한 경우"),
      @ApiResponse(
          responseCode = "401", description = "토큰이 유효하지 않거나 이미 탈퇴한 회원이거나 비밀번호가 일치하지 않은 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "409", description = "잔여 포인트가 있거나 진행중인 모임이 있는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Auth Api")
  @interface UserWithdrawalSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "사업가 회원탈퇴 api",
      description = "사업가의 회원탈퇴")
  @RequestBody(
      content = @Content(schema = @Schema(implementation = WithdrawalRequest.class)),
      description = "현재 계정의 비밀번호")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "탈퇴에 성공한 경우"),
      @ApiResponse(
          responseCode = "401", description = "토큰이 유효하지 않거나 이미 탈퇴한 회원이거나 비밀번호가 일치하지 않은 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "409", description = "접수받거나 진행중인 주문이 있는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Auth Api")
  @interface EntrepreneurWithdrawalSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "토큰 재발급 api",
      description = "만료된 토큰을 재발급")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "토큰 재발급에 성공한 경우",
          content = @Content(schema = @Schema(implementation = SignResponse.class))),
      @ApiResponse(
          responseCode = "401", description = "리프레시 토큰이 유효하지 않은 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Auth Api")
  @Tag(name = "Entrepreneur Auth Api")
  @interface RefreshTokenSwagger {}
}
