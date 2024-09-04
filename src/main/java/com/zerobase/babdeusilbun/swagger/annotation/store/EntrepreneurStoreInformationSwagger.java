package com.zerobase.babdeusilbun.swagger.annotation.store;

import com.zerobase.babdeusilbun.dto.PurchaseDto;
import com.zerobase.babdeusilbun.dto.PurchaseDto.MeetingPurchaseResponse;
import com.zerobase.babdeusilbun.dto.StoreDto.SimpleInformation;
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

public @interface EntrepreneurStoreInformationSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "등록한 상점 목록 조회 api",
      description = "로그인한 사업가가 등록한 상점 목록 조회")
  @Parameters(value = {
      @Parameter(name = "page", description = "조회하려는 페이지 번호(시작점: 0, 기본값: 0)", in = ParameterIn.QUERY),
      @Parameter(name = "size", description = "조회하려는 페이지의 요소 개수(최소:1, 기본값: 10)", in = ParameterIn.QUERY),
      @Parameter(name = "unprocessedOnly", description = "확인하지 않은 주문이 있는 상점만 조회 여부(기본값: false)")
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "등록한 상점 목록 조회에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = SimpleInformation.class)))),
      @ApiResponse(
          responseCode = "404", description = "사업가 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Store Information Api")
  @interface GetAllStoresByEntrepreneurSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "모임 주문 목록 조회 api",
      description = "상점에 들어온 주문 목록을 조회")
  @Parameters(value = {
      @Parameter(name = "storeId", description = "주문 목록을 확인하려는 상점 id", in = ParameterIn.PATH),
      @Parameter(name = "page", description = "조회하려는 주문 목록 페이지 번호(시작점: 0, 기본값: 0)", in = ParameterIn.QUERY),
      @Parameter(name = "size", description = "조회하려는 주문 목록 페이지의 요소 개수(최소:1, 기본값: 10)", in = ParameterIn.QUERY),
      @Parameter(name = "menuPage", description = "조회하려는 주문의 메뉴 목록 페이지 번호(시작점: 0, 기본값: 0)", in = ParameterIn.QUERY),
      @Parameter(name = "menuSize", description = "조회하려는 주문의 메뉴 목록 페이지의 요소 개수(최소:1, 기본값: 10)", in = ParameterIn.QUERY)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "등록한 상점 목록 조회에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = MeetingPurchaseResponse.class)))),
      @ApiResponse(
          responseCode = "404", description = "사업가, 상점 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403", description = "로그인한 사업가가 상점에 대한, 주문을 확인할 권한이 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Store Information Api")
  @interface GetAllMeetingPurchaseByStoreIdSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "모임 주문 상세 조회 api",
      description = "모임 하나에 대한 주문 메뉴 목록을 조회")
  @Parameters(value = {
      @Parameter(name = "storeId", description = "주문 목록을 확인하려는 상점 id", in = ParameterIn.PATH),
      @Parameter(name = "meetingId", description = "주문한 모임 id", in = ParameterIn.PATH),
      @Parameter(name = "page", description = "목록 페이지 번호(시작점: 0, 기본값: 0)", in = ParameterIn.QUERY),
      @Parameter(name = "size", description = "목록 페이지의 요소 개수(최소:1, 기본값: 10)", in = ParameterIn.QUERY)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "주문 상세 조회에 성공한 경우",
          content = @Content(schema = @Schema(implementation = PurchaseDto.MenuResponse.class))),
      @ApiResponse(
          responseCode = "404", description = "모임 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403", description = "로그인한 사업가가 주문을 확인할 권한이 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Store Information Api")
  @interface GetMeetingPurchaseByStoreIdAndMeetingId {}
}