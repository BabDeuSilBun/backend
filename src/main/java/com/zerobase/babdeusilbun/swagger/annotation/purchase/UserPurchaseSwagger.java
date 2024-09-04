package com.zerobase.babdeusilbun.swagger.annotation.purchase;

import com.zerobase.babdeusilbun.dto.SnapshotDto.PurchaseSnapshot;
import com.zerobase.babdeusilbun.dto.SnapshotDto.SubPurchaseSnapshot;
import com.zerobase.babdeusilbun.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

public @interface UserPurchaseSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "주문 후 공동 주문 스냅샷 목록 조회 api",
      description = "주문 후의 공동 주문에 대한 스냅샷(영수증) 목록 조회")
  @Parameters(value = {
      @Parameter(name = "meetingId", description = "공동 주문을 한 모임의 id", in = ParameterIn.PATH)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "공동 주문 스냅샷 목록 조회에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = SubPurchaseSnapshot.class)))),
      @ApiResponse(
          responseCode = "404", description = "모임, 이용자 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "400", description = "로그인한 이용자가 참가자가 아니거나 모임이 함께 식사 타입이 아닌 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Purchase Api")
  @interface GetTeamPurchaseSnapshotsSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "주문 후 개별 주문 스냅샷 목록 조회 api",
      description = "주문 후의 개별 주문에 대한 스냅샷(영수증) 목록 조회")
  @Parameters(value = {
      @Parameter(name = "meetingId", description = "개별 주문을 한 모임의 id", in = ParameterIn.PATH)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "개별 주문 스냅샷 목록 조회에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = SubPurchaseSnapshot.class)))),
      @ApiResponse(
          responseCode = "404", description = "모임, 이용자 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "400", description = "로그인한 이용자가 참가자가 아니거나 모임이 함께 배달 타입이 아닌 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Purchase Api")
  @interface GetIndividualPurchaseSnapshotsSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "주문 후 주문 스냅샷 조회 api",
      description = "주문 후 주문에 대한 스냅샷(영수증) 조회")
  @Parameter(name = "meetingId", description = "주문을 한 모임의 id", in = ParameterIn.PATH)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "주문 스냅샷 조회에 성공한 경우",
          content = @Content(schema = @Schema(implementation = PurchaseSnapshot.class))),
      @ApiResponse(
          responseCode = "404", description = "모임, 이용자 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "400", description = "로그인한 이용자가 참가자가 아닌 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Purchase Api")
  @interface GetPurchaseSnapshotsSwagger {}
}
