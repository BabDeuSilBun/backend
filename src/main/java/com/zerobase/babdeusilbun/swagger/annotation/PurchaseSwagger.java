package com.zerobase.babdeusilbun.swagger.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface PurchaseSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "주문 전 공동 주문 장바구니 조회")
  @Parameters(value = {
      @Parameter(name = "meetingId"),
      @Parameter(name = "pageable")
  })
  @Tag(name = "Purchase")
  @interface GetTeamPurchaseCartSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "주문 전 개별 주문 장바구니 조회")
  @Parameters(value = {
      @Parameter(name = "userDetails"),
      @Parameter(name = "meetingId"),
      @Parameter(name = "pageable")
  })
  @Tag(name = "Purchase")
  @interface GetIndividualPurchaseCartSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "주문 전 모임 배달비 조회")
  @Parameters(value = {
      @Parameter(name = "userDetails"),
      @Parameter(name = "meetingId")
  })
  @Tag(name = "Purchase")
  @interface GetDeliveryFeeInfoSwagger {}

}
