package com.zerobase.babdeusilbun.swagger.annotation.sign;

import com.zerobase.babdeusilbun.security.dto.EmailCheckDto;
import com.zerobase.babdeusilbun.security.dto.EmailCheckDto.Response;
import com.zerobase.babdeusilbun.security.dto.SignRequest;
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

public @interface EntrepreneurSignSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "사업가 이메일 중복확인 api",
      description = "회원가입하려는 사업가 이메일로 가입된 이메일이 있는지 확인")
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = EmailCheckDto.Request.class)),
      description = "확인할 이메일")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "이메일 중복확인 결과를 가져오는 데 성공한 경우",
          content = @Content(schema = @Schema(implementation = Response.class)))
  })
  @Tag(name = "Entrepreneur Sign Api")
  @interface CheckEmailForBusinessSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "사업가 회원가입 api",
      description = "사업가의 회원가입")
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = SignRequest.BusinessSignUp.class)),
      description = "사업가의 회원가입 정보")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "회원가입에 성공한 경우")
  })
  @Tag(name = "Entrepreneur Sign Api")
  @interface BusinessSignupSwagger {}
}
