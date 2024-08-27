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

public @interface MenuSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "메뉴 등록")
  @Parameters(value = {
      @Parameter(name = "entrepreneur"),
      @Parameter(name = "storeId")
  })
  @Tag(name = "Menu")
  @interface CreateMenuSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "메뉴 수정")
  @Parameters(value = {
      @Parameter(name = "entrepreneur"),
      @Parameter(name = "storeId")
  })
  @Tag(name = "Menu")
  @interface UpdateMenuSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "메뉴 삭제")
  @Parameters(value = {
      @Parameter(name = "entrepreneur"),
      @Parameter(name = "storeId")
  })
  @Tag(name = "Menu")
  @interface DeleteMenuSwagger {}




}
