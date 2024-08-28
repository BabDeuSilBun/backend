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

public @interface StoreCategorySwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "카테고리 조회")
  @Tag(name = "StoreCategory")
  @interface GetAllCategoriesSwagger {

  }


  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "상점에 카테고리 등록")
  @Parameter(name = "storeId")
  @Tag(name = "StoreCategory")
  @interface EnrollToCategorySwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "상점에 카테고리 삭제")
  @Parameter(name = "storeId")
  @Tag(name = "StoreCategory")
  @interface DeleteOnCategorySwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "상점별 카테고리 조회")
  @Parameters(value = {
      @Parameter(name = "storeId"),
      @Parameter(name = "page"),
      @Parameter(name = "size")
  })
  @Tag(name = "StoreCategory")
  @interface GetAllCategoriesByStoreSwagger {

  }

}
