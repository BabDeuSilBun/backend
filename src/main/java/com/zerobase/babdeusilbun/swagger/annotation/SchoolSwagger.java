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

public @interface SchoolSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "학교 검색", description = "(캠퍼스 포함)")
  @Parameters(value = {
      @Parameter(name = "schoolName"),
      @Parameter(name = "page"),
      @Parameter(name = "size")
  })
  @Tag(name = "School")
  @interface SearchSchoolAndCampusSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "학교 검색", description = "(캠퍼스 포함)")
  @Parameters(value = {
      @Parameter(name = "schoolId"),
      @Parameter(name = "page"),
      @Parameter(name = "size")
  })
  @Tag(name = "School")
  @interface SearchCampusBySchoolSwagger {}

}
