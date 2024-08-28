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

public @interface StoreHolidaySwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "상점별 휴무일 조회")
  @Parameters(value = {
      @Parameter(name = "storeId"),
      @Parameter(name = "page"),
      @Parameter(name = "size")
  })
  @Tag(name = "StoreHoliday")
  @interface GetAllHolidaysSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "상점 휴무일 등록")
  @Parameter(name = "storeId")
  @Tag(name = "StoreHoliday")
  @interface EnrollSchoolsToStoreSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "상점 휴무일 삭제")
  @Parameter(name = "storeId")
  @Tag(name = "StoreHoliday")
  @interface DeleteSchoolsOnStoreSwagger {

  }


}
