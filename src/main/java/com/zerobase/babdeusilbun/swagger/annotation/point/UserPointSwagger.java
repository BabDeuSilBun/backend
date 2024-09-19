package com.zerobase.babdeusilbun.swagger.annotation.point;

import com.zerobase.babdeusilbun.dto.PointDto.Response;
import com.zerobase.babdeusilbun.dto.PointDto.WithdrawalRequest;
import com.zerobase.babdeusilbun.dto.SnapshotDto.PointSnapshot;
import com.zerobase.babdeusilbun.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

public @interface UserPointSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "포인트 내역 조회 api",
      description = "포인트 입/출금 확인, sortCriteria로 상태 구분 가능")
  @Parameters(value = {
      @Parameter(name = "sortCriteria", description = "상태 구분 여부")
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "포인트 내역 조회에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = Response.class)))),
      @ApiResponse(
          responseCode = "404", description = "로그인한 이용자 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Point Api")
  @interface GetAllPointListSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "포인트 인출 api",
      description = "본인이 소지하고 있는 포인트 인출")
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = WithdrawalRequest.class)),
      description = "포인트 인출 금액")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "포인트 인출에 성공한 경우"),
      @ApiResponse(
          responseCode = "404", description = "로그인한 이용자 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "409", description = "인출할 포인트가 충분하지 않은 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Point Api")
  @interface WithdrawalPointSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "포인트 스냅샷 목록 조회 api",
      description = "포인트 스냅샷(영수증) 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "포인트 스냅샷 리스트 조회에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = PointSnapshot.class))))
  })
  @Tag(name = "User Point Api")
  @interface GetPointSnapshotListSwagger {}
}
