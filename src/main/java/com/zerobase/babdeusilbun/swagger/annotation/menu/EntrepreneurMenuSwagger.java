package com.zerobase.babdeusilbun.swagger.annotation.menu;

import com.zerobase.babdeusilbun.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface EntrepreneurMenuSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "메뉴 수정 api",
      description = "상점에 등록했던 메뉴 정보 수정")
  @Parameter(name = "menuId", description = "삭제하려는 메뉴의 id", in = ParameterIn.PATH)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "메뉴 정보 변경에 성공한 경우"),
      @ApiResponse(
          responseCode = "206", description = "메뉴 정보 변경에 부분적으로 성공한 경우"),
      @ApiResponse(
          responseCode = "404", description = "메뉴 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403", description = "메뉴를 수정할 권한이 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "409", description = "수정한 메뉴와 동일한 정보의 메뉴가 이미 있는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Menu Api")
  @interface UpdateMenuSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "메뉴 삭제 api",
      description = "상점에 등록했던 메뉴 삭제")
  @Parameter(name = "menuId", description = "삭제하려는 메뉴의 id", in = ParameterIn.PATH)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "메뉴 삭제에 성공한 경우"),
      @ApiResponse(
          responseCode = "404", description = "메뉴 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403", description = "메뉴를 삭제할 권한이 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Menu Api")
  @interface DeleteMenuSwagger {}
}
