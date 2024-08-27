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

public @interface StoreDetailSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "상점 정보 조회")
  @Parameters(value = {
      @Parameter(name = "storeId")
  })
  @Tag(name = "StoreDetail")
  @interface GetStoreInfoSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "가게별 메뉴 리스트 조회")
  @Parameters(value = {
      @Parameter(name = "storeId"),
      @Parameter(name = "page"),
      @Parameter(name = "size")
  })
  @Tag(name = "StoreDetail")
  @interface GetAllMenuSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "상점별 사업자 정보 조회")
  @Parameters(value = {
      @Parameter(name = "storeId")
  })
  @Tag(name = "StoreDetail")
  @interface GetEntrepreneurSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "썸네일 조회")
  @Parameters(value = {
      @Parameter(name = "storeId")
  })
  @Tag(name = "StoreDetail")
  @interface GetThumbnailSwagger {}



}
