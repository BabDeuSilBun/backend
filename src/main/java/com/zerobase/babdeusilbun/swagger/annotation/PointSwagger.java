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

public @interface PointSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "포인트 내역 조회")
  @Parameters(value = {
      @Parameter(name = "userDetails"),
      @Parameter(name = "sortCriteria"),
      @Parameter(name = "pageable")
  })
  @Tag(name = "Point")
  @interface GetAllPointListSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "포인트 인출")
  @Parameters(value = {
      @Parameter(name = "userDetails"),
      @Parameter(name = "request")
  })
  @Tag(name = "Point")
  @interface WithdrawalPointSwagger {}

}
