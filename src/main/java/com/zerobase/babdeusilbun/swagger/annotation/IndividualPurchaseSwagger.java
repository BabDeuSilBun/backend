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

public @interface IndividualPurchaseSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "개별주문 장바구니 등록")
  @Parameters(value = {
      @Parameter(name = "userDetails"),
      @Parameter(name = "meetingId"),
      @Parameter(name = "request")
  })
  @Tag(name = "IndividualPurchase")
  @interface CreateIndividualPurchaseSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "개별주문 장바구니 수정")
  @Parameters(value = {
      @Parameter(name = "userDetails"),
      @Parameter(name = "purchaseId"),
      @Parameter(name = "request")
  })
  @Tag(name = "IndividualPurchase")
  @interface UpdateIndividualPurchaseSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "개별주문 장바구니 삭제")
  @Parameters(value = {
      @Parameter(name = "userDetails"),
      @Parameter(name = "purchaseId")
  })
  @Tag(name = "IndividualPurchase")
  @interface DeleteIndividualPurchaseSwagger {}

}
