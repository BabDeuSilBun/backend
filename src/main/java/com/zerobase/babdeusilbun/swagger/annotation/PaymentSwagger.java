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

public @interface PaymentSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "모임장, 모임원의 결제 진행 요청")
  @Parameters(value = {
      @Parameter(name = "meetingId"),
      @Parameter(name = "purchaseId")
  })
  @Tag(name = "Payment")
  @interface PaymentProcessSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "결제 진행 후 결제 성공 확인 요청")
  @Parameters(value = {
      @Parameter(name = "meetingId"),
      @Parameter(name = "purchaseId")
  })
  @Tag(name = "Payment")
  @interface PaymentConfirmSwagger {}

}
