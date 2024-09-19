package com.zerobase.babdeusilbun.swagger.annotation.meeting;

import com.zerobase.babdeusilbun.dto.MeetingDto;
import com.zerobase.babdeusilbun.dto.PurchaseDto.DeliveryFeeResponse;
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

public @interface UserMeetingInformationSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "모임리스트 목록 조회/검색 api",
      description = "입력된 학교의 schoolId에 따라 참여 가능한 모임리스트 목록을 원하는 정렬로 조회\n"
          + "단, schoolId 미입력 시 로그인한 이용자의 학교 기준으로 검색\n\n"
          + "정렬 종류: 결제 마감 시간 순(`DEADLINE`), 배송시간 짧은 순(`DELIVERY_TIME`), 배달비 적은 순(`DELIVERY_FEE`), 최소주문금액 낮은 순(`MIN_PRICE`)"
  )
  @Parameters(value = {
      @Parameter(name = "schoolId", description = "모임 검색의 기준이 될 학교의 id"),
      @Parameter(name = "sortCriteria", description = "정렬 기준"),
      @Parameter(name = "searchMenu", description = "메뉴 검색"),
      @Parameter(name = "categoryFilter", description = "카테고리 지정")
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "모임리스트 목록 조회/검색에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = MeetingDto.class)))),
      @ApiResponse(
          responseCode = "404", description = "입력한 학교 정보가 없는데 로그인한 이용자를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Meeting Information Api")
  @interface GetAllMeetingListSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "주문 전 모임 배달비 조회 api",
      description = "모임의 선결제 배달비 조회")
  @Parameter(name = "meetingId", description = "조회하려는 모임의 id", in = ParameterIn.PATH)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "배달비 조회에 성공한 경우",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeliveryFeeResponse.class))),
      @ApiResponse(
          responseCode = "404", description = "모임 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "400", description = "모임이 주문 전 상태가 아니거나 모임의 참가자가 아닌 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Meeting Information Api")
  @interface GetDeliveryFeeInfoSwagger {}
}
