package com.zerobase.babdeusilbun.swagger.annotation.purchase;

import com.zerobase.babdeusilbun.dto.ChatDto;
import com.zerobase.babdeusilbun.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

public @interface EntrepreneurPurchaseSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "모임 주문 승인 api",
      description = "자신의 상점으로 들어온 주문 승인")
  @Parameters(value = {
      @Parameter(name = "meetingId", description = "주문을 보낸 모임 id", in = ParameterIn.PATH)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "주문 승인에 성공한 경우"),
      @ApiResponse(
          responseCode = "404", description = "모임, 사업가 정보, 승인하고 알림을 보내야하는 결제 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403", description = "로그인한 이용자가 승인할 수 없는 모임인 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Purchase Api")
  @interface ConfirmMeetingPurchaseByMeetingIdSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "모임 주문 거절 api",
      description = "자신의 상점으로 들어온 주문 거절")
  @Parameters(value = {
      @Parameter(name = "meetingId", description = "주문을 보낸 모임 id", in = ParameterIn.PATH)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "주문 거절에 성공한 경우"),
      @ApiResponse(
          responseCode = "404", description = "모임, 사업가 정보, 거절하고 알림을 보내야하는 결제 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403", description = "로그인한 이용자가 거절할 수 없는 모임인 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Purchase Api")
  @interface DenyMeetingPurchaseSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "모임 주문 조리 완료 알림 api",
      description = "자신의 상점으로 들어온 주문에 대한 조리가 완료되었음을 알림")
  @Parameters(value = {
      @Parameter(name = "meetingId", description = "주문을 보낸 모임 id", in = ParameterIn.PATH)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "조리 완료 알림에 성공한 경우"),
      @ApiResponse(
          responseCode = "404", description = "모임, 사업가 정보, 조리 완료 시간을 기록할 주문 처리과정 시간을 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403", description = "로그인한 이용자가 상태변경할 수 없는 모임인 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Purchase Api")
  @interface CompleteMeetingPurchaseSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "모임 주문 지연 사유 전송 api",
      description = "자신의 상점으로 들어온 주문을 승인하고 완료까지 시간이 오래 지연될 경우 지연 사유 전송")
  @Parameters(value = {
      @Parameter(name = "meetingId", description = "주문을 보낸 모임 id", in = ParameterIn.PATH)
  })
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = ChatDto.Request.class)),
      description = "지연 사유")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "지연 사유 전송에 성공한 경우"),
      @ApiResponse(
          responseCode = "404", description = "모임, 사업가 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403", description = "로그인한 이용자가 알림을 보낼 수 없는 모임인 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Purchase Api")
  @interface SendMessageForDelayMeetingPurchaseSwagger {}
}
