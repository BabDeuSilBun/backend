package com.zerobase.babdeusilbun.swagger.annotation.meeting;

import com.zerobase.babdeusilbun.dto.EvaluateDto.EvaluateParticipantRequest;
import com.zerobase.babdeusilbun.dto.MeetingRequest.Create;
import com.zerobase.babdeusilbun.dto.MeetingRequest.Update;
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

public @interface UserMeetingManagementSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "모임 생성 api",
      description = "모임 생성")
  @RequestBody(
      content = @Content(schema = @Schema(implementation = Create.class)),
      description = "모임 생성 정보")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "모임 생성에 성공한 경우"),
      @ApiResponse(
          responseCode = "404", description = "로그인한 이용자를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Meeting Management Api")
  @interface CreateMeetingSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "가게 주문 전 모임 정보 수정 api",
      description = "가게에 주문하기 전에 모임의 정보를 수정")
  @Parameter(name = "meetingId", description = "정보를 수정하려는 모임의 id", in = ParameterIn.PATH)
  @RequestBody(
      content = @Content(schema = @Schema(implementation = Update.class)),
      description = "수정할 내용")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "정보 변경에 성공한 경우"),
      @ApiResponse(
          responseCode = "404", description = "로그인한 이용자 또는 모임 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "400", description = "모임 정보를 수정하려는 이용자가 모임장이 아니거나 모임 상태가 수정 가능 상태가 아닌 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Meeting Management Api")
  @interface UpdateMeetingInfoSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "모임 탈퇴/취소 api",
      description = "참여한/생성한 모임에 대해 탈퇴/취소")
  @Parameter(name = "meetingId", description = "탈퇴/취소하려는 모임의 id", in = ParameterIn.PATH)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "모임 탈퇴/취소에 성공한 경우"),
      @ApiResponse(
          responseCode = "404", description = "로그인한 이용자 또는 모임 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "400", description = "모임 상태가 탈퇴 가능 상태가 아닌 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Meeting Management Api")
  @interface WithdrawMeetingSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "모임원 평가 api",
      description = "모임이 완료된 이후 함께 했던 모임원에 대해 평가")
  @Parameters(value = {
      @Parameter(name = "meetingId", description = "평가하려는 이용자가 속한 모임의 id", in = ParameterIn.PATH),
      @Parameter(name = "participantId", description = "평가하려는 일반 이용자의 id", in = ParameterIn.PATH)
  })
  @RequestBody(
      content = @Content(schema = @Schema(implementation = EvaluateParticipantRequest.class)),
      description = "모임원 평가 내용")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "모임원 평가에 성공한 경우"),
      @ApiResponse(
          responseCode = "404", description = "로그인한 이용자, 평가하려는 이용자, 또는 모임 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "400", description = "모임원을 평가할 수 있는 상태가 아니거나 평가자/평가대상이 모임 참여원이 아닌 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "409", description = "모임원을 이미 평가한 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Meeting Management Api")
  @interface EvaluateParticipantSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "모임 주문 상점으로 전송 api",
      description = "모임 주문 조건이 충족되었을 때 모임장이 상점으로 주문 내역을 전송하는 api")
  @Parameters(value = {
      @Parameter(name = "meetingId", description = "평가하려는 이용자가 속한 모임의 id", in = ParameterIn.PATH)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "주문 전송에 성공한 경우"),
      @ApiResponse(
          responseCode = "404", description = "모임 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "400", description = "모임장이 아니거나 주문 가능 여건을 충족하지 못한 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Meeting Management Api")
  @interface SendPurchaseToStoreSwagger {}
}
