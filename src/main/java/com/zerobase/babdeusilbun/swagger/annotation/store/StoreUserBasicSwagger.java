package com.zerobase.babdeusilbun.swagger.annotation.store;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface StoreUserBasicSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "주문 가능 가게 리스트 검색/조회")
  @Parameters(value = {
      @Parameter(name = "categoryList"),
      @Parameter(name = "searchMenu"),
      @Parameter(name = "storeId"),
      @Parameter(name = "sortCriteria"),
  })
  @Tag(name = "StoreUserBasic")
  @interface GetAvailStoreListSwagger {}

}
