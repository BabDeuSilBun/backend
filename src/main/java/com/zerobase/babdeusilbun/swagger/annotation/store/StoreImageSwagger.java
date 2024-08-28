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

public @interface StoreImageSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "상점 이미지 전체 조회")
  @Parameters(value = {
      @Parameter(name = "storeId"),
      @Parameter(name = "page"),
      @Parameter(name = "size")
  })
  @Tag(name = "StoreImage")
  @interface GetAllImagesSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "상점 이미지 등록")
      @Parameter(name = "storeId")
  @Tag(name = "StoreImage")
  @interface EnrollImagesToStoreSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "상점 이미지 삭제")
  @Parameters(value = {
      @Parameter(name = "storeId"),
      @Parameter(name = "imageId")
  })
  @Tag(name = "StoreImage")
  @interface DeleteImageOnStoreSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "상점 이미지 설정변경")
  @Parameters(value = {
      @Parameter(name = "storeId"),
      @Parameter(name = "imageId")
  })
  @Tag(name = "StoreImage")
  @interface UpdateStoreImageInformationSwagger {}


}
