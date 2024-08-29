package com.zerobase.babdeusilbun.swagger.annotation.sign;

import com.zerobase.babdeusilbun.dto.SignDto;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyCodeRequest;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyCodeResponse;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyEmailRequest;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyPasswordResponse;
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

public @interface SignSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "비밀번호 확인 api",
      description = "입력한 비밀번호값과 현재 계정의 비밀번호 값이 일치하는지 확인")
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = SignDto.VerifyPasswordRequest.class)),
      description = "현재 계정의 비밀번호")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "비밀번호 확인 결과를 가져오는 데 성공한 경우",
          content = @Content(schema = @Schema(implementation = VerifyPasswordResponse.class)))
  })
  @Tag(name = "Entrepreneur Sign Api")
  @Tag(name = "User Sign Api")
  @interface PasswordConfirmSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "이메일 유효성 검증 이메일 전송 api",
      description = "입력한 이메일 주소로 이메일 유효성 검증 코드가 담긴 이메일 전송")
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = VerifyEmailRequest.class)),
      description = "유효한 이메일 주소")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "이메일 전송에 성공한 경우")
  })
  @Tag(name = "Entrepreneur Sign Api")
  @Tag(name = "User Sign Api")
  @Tag(name = "Common Sign Api")
  @interface SendEmailVerifyCodeSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "이메일 유효성 검증 코드 검증 api",
      description = "메일로 발송된 유효성 검증 코드와 입력된 코드가 일치하는지 확인")
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = VerifyCodeRequest.class)),
      description = "이몌일로 전송받은 검증 코드")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "이메일 유효성 검증 결과를 가져오는 데 성공한 경우",
          content = @Content(schema = @Schema(implementation = VerifyCodeResponse.class)))
  })
  @Tag(name = "Entrepreneur Sign Api")
  @Tag(name = "User Sign Api")
  @Tag(name = "Common Sign Api")
  @interface ConfirmEmailVerifyCodeSwagger {}
}
