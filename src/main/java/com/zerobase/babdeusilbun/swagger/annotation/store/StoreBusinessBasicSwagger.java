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

public @interface StoreBusinessBasicSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "등록한 상점 리스트 조회")
  @Parameters(value = {
      @Parameter(name = "page"),
      @Parameter(name = "size"),
      @Parameter(name = "unprocessedOnly")
  })
  @Tag(name = "StoreBusinessBasic")
  @interface GetAllStoresByEntrepreneurSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "상점 등록")
  @Tag(name = "StoreBusinessBasic")
  @interface CreateStoreSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "상점 정보 수정")
  @Parameter(name = "storeId")
  @Tag(name = "StoreBusinessBasic")
  @interface UpdateStoreInformationSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "상점 삭제")
  @Parameter(name = "storeId")
  @Tag(name = "StoreBusinessBasic")
  @interface DeleteStoreSwagger {

  }

}