package com.zerobase.babdeusilbun.swagger.annotation.payment;

import com.zerobase.babdeusilbun.dto.PaymentDto.ConfirmRequest;
import com.zerobase.babdeusilbun.dto.PaymentDto.ConfirmResponse;
import com.zerobase.babdeusilbun.dto.PaymentDto.ProcessRequest;
import com.zerobase.babdeusilbun.dto.PaymentDto.ProcessResponse;
import com.zerobase.babdeusilbun.dto.SnapshotDto.PaymentSnapshot;
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

public @interface UserPaymentSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "결제 스냅샷 조회 api",
      description = "결제한 내역(영수증) 확인")
  @Parameter(name = "meetingId", description = "결제한 모임의 id", in = ParameterIn.PATH)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "결제 스냅샷 조회에 성공한 경우",
          content = @Content(schema = @Schema(implementation = PaymentSnapshot.class))),
      @ApiResponse(
          responseCode = "404", description = "로그인한 이용자, 모임 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "400", description = "결제 스냅샷을 조회하려는 이용자가 모임 참여자가 아닌 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Payment Api")
  @interface GetPaymentSnapshotsSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "모임장, 모임 참여자의 결제 진행 요청 api",
      description = "장바구니에 담은 메뉴들 결제 요청, 모임 참여와 동치")
  @Parameters(value = {
      @Parameter(name = "meetingId", description = "참여할 모임의 id", in = ParameterIn.PATH),
      @Parameter(name = "purchaseId", description = "장바구니가 등록된 주문 id", in = ParameterIn.PATH),
  })
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = ProcessRequest.class)),
      description = "결제 요청 정보, 포인트 사용 금액")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "결제 요청에 성공한 경우",
          content = @Content(schema = @Schema(implementation = ProcessResponse.class))),
      @ApiResponse(
          responseCode = "404", description = "로그인한 이용자, 모임 정보, 구매 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "400", description = "모임의 참가자가 아니거나 주문과 모임이 올바른 관계가 아니거나, 모임의 상태와 주문 상태가 결제 요청할 상태가 아닌 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "409", description = "모임이 이미 삭제되거나 사용 가능한 포인트가 충분하지 않은 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Payment Api")
  @interface PaymentProcessSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "결제 성공여부 확인 요청 api",
      description = "요청한 결제가 제대로 이루어졌는지 확인")
  @Parameters(value = {
      @Parameter(name = "meetingId", description = "참여할 모임의 id", in = ParameterIn.PATH),
      @Parameter(name = "purchaseId", description = "결제한 주문 id", in = ParameterIn.PATH),
  })
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = ConfirmRequest.class)),
      description = "확인할 결제 요청 정보")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "결제 확인 요청이 잘 이뤄진 경우",
          content = @Content(schema = @Schema(implementation = ConfirmResponse.class))),
      @ApiResponse(
          responseCode = "404", description = "로그인한 이용자, 모임 정보, 구매 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "400", description = "모임의 참가자가 아니거나 주문과 모임이 올바른 관계가 아니거나, 모임의 상태와 주문 상태가 결제 요청할 상태가 아닌 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "409", description = "결제 아이디가 다른 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Payment Api")
  @interface PaymentConfirmSwagger {}
}
