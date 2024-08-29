package com.zerobase.babdeusilbun.swagger.annotation.profile;

import com.zerobase.babdeusilbun.dto.EvaluateDto.MyEvaluates;
import com.zerobase.babdeusilbun.dto.UserDto.MyPage;
import com.zerobase.babdeusilbun.dto.UserDto.UpdateAccount;
import com.zerobase.babdeusilbun.dto.UserDto.UpdateAddress;
import com.zerobase.babdeusilbun.dto.UserDto.UpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.multipart.MultipartFile;

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
          content = @Content(schema = @Schema(implementation = MyPage.class)))
  })
  @Tag(name = "User Profile Api")
  @interface GetMyProfileSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "회원 정보 수정 api",
      description = "로그인한 일반 이용자 권한의 이용자 정보 수정")
  @Parameter(
      name = "file", description = "10MB 이하의 변경할 이미지(없다면 입력X)",
      content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = MultipartFile.class)))
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = UpdateRequest.class)),
      description = "회원 정보 수정사항")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "회원 정보 수정에 성공한 경우"),
      @ApiResponse(
          responseCode = "206", description = "회원 정보 수정에 부분적으로 성공한 경우")
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
