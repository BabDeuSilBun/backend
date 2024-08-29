package com.zerobase.babdeusilbun.swagger.annotation.menu;

import com.zerobase.babdeusilbun.dto.MenuDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.web.multipart.MultipartFile;

public @interface EntrepreneurMenuSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "메뉴 수정 api",
      description = "상점에 등록했던 메뉴 정보 수정")
  @Parameters(value = {
      @Parameter(
          name = "file", description = "10MB 이하의 변경할 이미지(없다면 입력X)",
          content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = MultipartFile.class))),
      @Parameter(name = "menuId", description = "변경하려는 메뉴의 id", in = ParameterIn.PATH)
  })
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = MenuDto.UpdateRequest.class)),
      description = "메뉴 수정사항")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "메뉴 정보 변경에 성공한 경우"),
      @ApiResponse(
          responseCode = "206", description = "메뉴 정보 변경에 부분적으로 성공한 경우")
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
  })
  @Tag(name = "Entrepreneur Menu Api")
  @interface DeleteMenuSwagger {}
}
