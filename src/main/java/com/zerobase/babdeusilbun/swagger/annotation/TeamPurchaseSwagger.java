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

public @interface TeamPurchaseSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "공통주문 장바구니 등록")
  @Parameter(name = "meetingId")
  @Tag(name = "TeamPurchase")
  @interface CreateTeamPurchaseSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "공통주문 장바구니 수정")
  @Parameter(name = "purchaseId")
  @Tag(name = "TeamPurchase")
  @interface UpdateTeamPurchaseSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "공통주문 장바구니 삭제")
  @Parameter(name = "purchaseId")
  @Tag(name = "TeamPurchase")
  @interface DeleteTeamPurchaseSwagger {

  }

}
