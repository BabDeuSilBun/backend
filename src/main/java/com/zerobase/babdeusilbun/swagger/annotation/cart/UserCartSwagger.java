package com.zerobase.babdeusilbun.swagger.annotation.cart;

import com.zerobase.babdeusilbun.dto.IndividualPurchaseDto;
import com.zerobase.babdeusilbun.dto.PurchaseDto.PurchaseResponse;
import com.zerobase.babdeusilbun.dto.TeamPurchaseDto.CreateRequest;
import com.zerobase.babdeusilbun.dto.TeamPurchaseDto.UpdateRequest;
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

public @interface UserCartSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "주문 전 현재 공동 장바구니 조회 api",
      description = "아직 주문하기 이전인 모임의 공동 장바구니 조회")
  @Parameters(value = {
      @Parameter(name = "meetingId", description = "확인하려는 공동 장바구니가 해당된 모임의 id", in = ParameterIn.PATH)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "공동 장바구니 조회에 성공한 경우",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = PurchaseResponse.class))),
      @ApiResponse(
          responseCode = "404", description = "모임을 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(
          responseCode = "400", description = "공동 장바구니를 가질 수 없는 모임 유형이거나 모임이 주문 전 상태가 아닌 경우",
          content = @Content(schema = @Schema(implementation = String.class)))
  })
  @Tag(name = "User Team Cart Api")
  @interface GetTeamPurchaseCartSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "공동주문 장바구니에 메뉴 등록 api",
      description = "모임장이 참여원과 함께 계산할 메뉴를 장바구니에 등록")
  @Parameters(value = {
      @Parameter(name = "meetingId", description = "공동 장바구니가 해당된 모임의 id", in = ParameterIn.PATH),
  })
  @RequestBody(
      content = @Content(schema = @Schema(implementation = CreateRequest.class)),
      description = "장바구니에 담을 메뉴의 id와 개수 정보")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "장바구니에 메뉴가 성공적으로 담긴 경우"),
      @ApiResponse(
          responseCode = "404", description = "로그인한 이용자, 모임, 메뉴를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(
          responseCode = "400", description = "api를 이용하는 사용자가 모임장이 아니거나 막 생성된 모임이 아닌 경우",
          content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(
          responseCode = "409", description = "담으려는 메뉴를 주문할 수 없는 상점이거나 이미 같은 메뉴가 등록되어있는 경우",
          content = @Content(schema = @Schema(implementation = String.class)))
  })
  @Tag(name = "User Team Cart Api")
  @interface CreateTeamPurchaseSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "공동주문 장바구니에 담긴 메뉴 수량 수정 api",
      description = "공동주문 장바구니에 담긴 메뉴의 수량을 수정")
  @Parameters(value = {
      @Parameter(name = "purchaseId", description = "수량을 변경할 주문의 id", in = ParameterIn.PATH)
  })
  @RequestBody(
      content = @Content(schema = @Schema(implementation = UpdateRequest.class)),
      description = "변경할 수량 정보")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "변경에 성공할 경우"),
      @ApiResponse(
          responseCode = "404", description = "로그인한 이용자, 모임, 메뉴를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(
          responseCode = "400", description = "api를 이용하는 사용자가 모임장이 아니거나 막 생성된 모임이 아닌 경우",
          content = @Content(schema = @Schema(implementation = String.class)))
  })
  @Tag(name = "User Team Cart Api")
  @interface UpdateTeamPurchaseSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "공동주문 장바구니에 담긴 메뉴의 주문 제거 api",
      description = "공동주문 장바구니에 담긴 메뉴의 주문을 제거")
  @Parameters(value = {
      @Parameter(name = "purchaseId", description = "제거할 주문의 id", in = ParameterIn.PATH)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "주문 삭제에 성공한 경우"),
      @ApiResponse(
          responseCode = "404", description = "로그인한 이용자, 모임, 주문 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(
          responseCode = "400", description = "api를 이용하는 사용자가 모임장이 아니거나 막 생성된 모임이 아닌 경우",
          content = @Content(schema = @Schema(implementation = String.class)))
  })
  @Tag(name = "User Team Cart Api")
  @interface DeleteTeamPurchaseSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "주문 전 현재 개인 장바구니 조회 api",
      description = "아직 주문하기 이전인 모임의 개인 장바구니 조회")
  @Parameters(value = {
      @Parameter(name = "meetingId", description = "확인하려는 개인 장바구니가 해당된 모임의 id", in = ParameterIn.PATH)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "개인 장바구니 조회에 성공한 경우",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = PurchaseResponse.class))),
      @ApiResponse(
          responseCode = "404", description = "로그인한 이용자, 모임, 진행중인 주문을 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(
          responseCode = "400", description = "개인 장바구니를 가질 수 없는 모임 유형이거나 모임이 주문 전 상태가 아닌 경우",
          content = @Content(schema = @Schema(implementation = String.class)))
  })
  @Tag(name = "User Individual Cart Api")
  @interface GetIndividualPurchaseCartSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "개별주문 장바구니에 메뉴 등록 api",
      description = "모임에 참여할 사람이 각자 계산할 메뉴를 장바구니에 등록")
  @Parameters(value = {
      @Parameter(name = "meetingId", description = "개인 장바구니가 해당된 모임의 id", in = ParameterIn.PATH)
  })
  @RequestBody(
      content = @Content(schema = @Schema(implementation = IndividualPurchaseDto.CreateRequest.class)),
      description = "장바구니에 담을 메뉴의 id와 개수 정보")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "장바구니에 메뉴가 성공적으로 담긴 경우"),
      @ApiResponse(
          responseCode = "404", description = "로그인한 이용자, 모임, 메뉴를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(
          responseCode = "400", description = "주문 전의 모임이 아니거나 이미 참여한 모임인 경우",
          content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(
          responseCode = "409", description = "담으려는 메뉴를 주문할 수 없는 상점이거나 이미 같은 메뉴가 등록되어있는 경우",
          content = @Content(schema = @Schema(implementation = String.class)))
  })
  @Tag(name = "User Individual Cart Api")
  @interface CreateIndividualPurchaseSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "개별주문 장바구니에 담긴 메뉴 수량 수정 api",
      description = "개별주문 장바구니에 담긴 메뉴의 수량을 수정")
  @Parameters(value = {
      @Parameter(name = "purchaseId", description = "수량을 변경할 주문의 id", in = ParameterIn.PATH)
  })
  @RequestBody(
      content = @Content(schema = @Schema(implementation = IndividualPurchaseDto.UpdateRequest.class)),
      description = "변경할 수량 정보")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "변경에 성공할 경우"),
      @ApiResponse(
          responseCode = "404", description = "로그인한 이용자, 모임, 메뉴를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(
          responseCode = "403", description = "주문을 변경할 권한이 없는 경우",
          content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(
          responseCode = "400", description = "api를 이용하는 사용자가 모임장이 아니거나 주문 전의 모임이 아닌 경우",
          content = @Content(schema = @Schema(implementation = String.class)))
  })
  @Tag(name = "User Individual Cart Api")
  @interface UpdateIndividualPurchaseSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "개별주문 장바구니에 담긴 메뉴의 주문 제거 api",
      description = "개별주문 장바구니에 담긴 메뉴의 주문을 제거")
  @Parameters(value = {
      @Parameter(name = "purchaseId", description = "제거할 주문의 id", in = ParameterIn.PATH)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "주문 삭제에 성공한 경우"),
      @ApiResponse(
          responseCode = "404", description = "로그인한 이용자, 주문 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(
          responseCode = "403", description = "주문을 삭제할 권한이 없는 경우",
          content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(
          responseCode = "400", description = "주문 전의 모임이 아닌 경우",
          content = @Content(schema = @Schema(implementation = String.class)))
  })
  @Tag(name = "User Individual Cart Api")
  @interface DeleteIndividualPurchaseSwagger {}
}
