package com.zerobase.babdeusilbun.swagger.annotation.store;

import com.zerobase.babdeusilbun.dto.CategoryDto.IdsRequest;
import com.zerobase.babdeusilbun.dto.HolidayDto.HolidaysRequest;
import com.zerobase.babdeusilbun.dto.SchoolDto;
import com.zerobase.babdeusilbun.dto.StoreDto.IdResponse;
import com.zerobase.babdeusilbun.dto.StoreDto.UpdateRequest;
import com.zerobase.babdeusilbun.dto.StoreImageDto;
import com.zerobase.babdeusilbun.exception.ErrorResponse;
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

public @interface EntrepreneurStoreManagementSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "상점 등록 api",
      description = "사업가가 새로 상점을 등록")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "상점 등록에 성공한 경우",
          content = @Content(schema = @Schema(implementation = IdResponse.class))),
      @ApiResponse(
          responseCode = "206", description = "상점은 등록되었으나 이미지가 부분만 업로드된 경우",
          content = @Content(schema = @Schema(implementation = IdResponse.class))),
      @ApiResponse(
          responseCode = "404", description = "사업가 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "409", description = "로그인한 사업가가 등록하려는 상점이 이미 존재하는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Store Management Api")
  @interface CreateStoreSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "메뉴 등록 api",
      description = "사업가가 자신의 상점에 새로운 메뉴를 등록")
  @Parameter(name = "storeId", description = "메뉴를 등록할 상점의 id", in = ParameterIn.PATH)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "메뉴 등록에 성공한 경우"),
      @ApiResponse(
          responseCode = "206", description = "메뉴는 등록되었으나 이미지가 업로드되지 않은 경우"),
      @ApiResponse(
          responseCode = "404", description = "사업가, 상점 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "409", description = "등록하려는 메뉴가 이미 상점에 등록되어 있는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Store Management Api")
  @interface CreateMenuSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "상점에 카테고리 등록 api", 
      description = "상점에 상점이 분류되는 카테고리를 등록")
  @Parameter(name = "storeId", description = "카테고리를 등록할 상점의 id", in = ParameterIn.PATH)
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = IdsRequest.class)),
      description = "등록할 카테고리 id 목록")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "카테고리 등록에 성공한 경우"),
      @ApiResponse(
          responseCode = "206", description = "카테고리가 요청값 중 일부만 등록된 경우"),
      @ApiResponse(
          responseCode = "304", description = "요청은 성공했으나 변화가 없는 경우"),
      @ApiResponse(
          responseCode = "404", description = "사업가, 상점 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403", description = "로그인한 사업가가 상점에 대한 권한이 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Store Management Api")
  @interface EnrollToCategorySwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "상점에서 카테고리 삭제 api",
      description = "상점에서 상점이 분류되는 카테고리를 제거")
  @Parameter(name = "storeId", description = "카테고리를 삭제할 상점의 id", in = ParameterIn.PATH)
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = IdsRequest.class)),
      description = "삭제할 카테고리 id 목록")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "카테고리 삭제에 성공한 경우"),
      @ApiResponse(
          responseCode = "206", description = "카테고리가 요청값 중 일부만 삭제된 경우"),
      @ApiResponse(
          responseCode = "304", description = "요청은 성공했으나 변화가 없는 경우"),
      @ApiResponse(
          responseCode = "404", description = "사업가, 상점 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403", description = "로그인한 사업가가 상점에 대한 권한이 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Store Management Api")
  @interface DeleteOnCategorySwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "상점에 배달가능 캠퍼스 등록 api",
      description = "상점에 상점이 배달가능한 캠퍼스를 등록")
  @Parameter(name = "storeId", description = "캠퍼스를 등록할 상점의 id", in = ParameterIn.PATH)
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = SchoolDto.IdsRequest.class)),
      description = "등록할 캠퍼스 id 목록")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "캠퍼스 등록에 성공한 경우"),
      @ApiResponse(
          responseCode = "206", description = "캠퍼스가 요청값 중 일부만 등록된 경우"),
      @ApiResponse(
          responseCode = "304", description = "요청은 성공했으나 변화가 없는 경우"),
      @ApiResponse(
          responseCode = "404", description = "사업가, 상점 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403", description = "로그인한 사업가가 상점에 대한 권한이 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Store Management Api")
  @interface EnrollSchoolsToStoreSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "상점에 배달가능 캠퍼스 삭제 api",
      description = "상점에 등록된 상점이 배달가능한 캠퍼스를 제거")
  @Parameter(name = "storeId", description = "캠퍼스를 삭제할 상점의 id", in = ParameterIn.PATH)
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = SchoolDto.IdsRequest.class)),
      description = "삭제할 캠퍼스 id 목록")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "캠퍼스 삭제에 성공한 경우"),
      @ApiResponse(
          responseCode = "206", description = "캠퍼스가 요청값 중 일부만 삭제된 경우"),
      @ApiResponse(
          responseCode = "304", description = "요청은 성공했으나 변화가 없는 경우"),
      @ApiResponse(
          responseCode = "404", description = "사업가, 상점 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403", description = "로그인한 사업가가 상점에 대한 권한이 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Store Management Api")
  @interface DeleteSchoolsOnStoreSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "상점에 휴무일 등록 api",
      description = "상점에 상점이 운영되지 않는 요일을 등록")
  @Parameter(name = "storeId", description = "휴무일을 등록할 상점의 id", in = ParameterIn.PATH)
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = HolidaysRequest.class)),
      description = "쉬는 날로 등록할 요일 목록")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "휴무일 등록에 성공한 경우"),
      @ApiResponse(
          responseCode = "206", description = "요일 요청값 중 일부만 등록된 경우"),
      @ApiResponse(
          responseCode = "304", description = "요청은 성공했으나 변화가 없는 경우"),
      @ApiResponse(
          responseCode = "404", description = "사업가, 상점 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403", description = "로그인한 사업가가 상점에 대한 권한이 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Store Management Api")
  @interface EnrollHolidaysToStoreSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "상점에 휴무일 삭제 api",
      description = "상점에 등록된 상점이 운영되지 않는 요일을 제거")
  @Parameter(name = "storeId", description = "휴무일을 삭제할 상점의 id", in = ParameterIn.PATH)
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = HolidaysRequest.class)),
      description = "쉬는 날에서 제외할 요일 목록")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "휴무일 삭제에 성공한 경우"),
      @ApiResponse(
          responseCode = "206", description = "요일 요청값 중 일부만 삭제된 경우"),
      @ApiResponse(
          responseCode = "304", description = "요청은 성공했으나 변화가 없는 경우"),
      @ApiResponse(
          responseCode = "404", description = "사업가, 상점 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403", description = "로그인한 사업가가 상점에 대한 권한이 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Store Management Api")
  @interface DeleteHolidaysOnStoreSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "상점 정보 수정 api",
      description = "상점의 기본 정보 수정")
  @Parameter(name = "storeId", description = "정보를 수정할 상점의 id", in = ParameterIn.PATH)
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = UpdateRequest.class)),
      description = "변경할 상점 정보")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "상점 정보 변경에 성공한 경우"),
      @ApiResponse(
          responseCode = "404", description = "사업가, 상점 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403", description = "로그인한 사업가가 상점에 대한 권한이 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Store Management Api")
  @interface UpdateStoreInformationSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "상점 이미지 등록 api",
      description = "상점에 이미지 등록(단, 기존 상점 이미지와 합하여 입력된 이미지 개수가 3장을 넘어갈 경우 등록 불가)")
  @Parameter(name = "storeId", description = "이미지를 등록할 상점의 id", in = ParameterIn.PATH)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "이미지 등록에 성공한 경우"),
      @ApiResponse(
          responseCode = "206", description = "등록 요청값 중 일부만 성공한 경우"),
      @ApiResponse(
          responseCode = "304", description = "요청은 성공했으나 변화가 없는 경우"),
      @ApiResponse(
          responseCode = "404", description = "사업가, 상점 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403", description = "로그인한 사업가가 상점에 대한 권한이 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Store Management Api")
  @interface EnrollImagesToStoreSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "상점 이미지 삭제 api",
      description = "상점에 등록된 이미지 삭제")
  @Parameters(value = {
      @Parameter(name = "storeId", description = "이미지를 삭제할 상점의 id", in = ParameterIn.PATH),
      @Parameter(name = "imageId", description = "삭제할 이미지의 id", in = ParameterIn.PATH)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204", description = "이미지 삭제에 성공한 경우"),
      @ApiResponse(
          responseCode = "206", description = "상점 이미지는 제거되었지만 S3스토리지에는 남아있는 경우"),
      @ApiResponse(
          responseCode = "404", description = "사업가, 상점, 이미지 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403", description = "로그인한 사업가가 상점에 대한 권한이 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Store Management Api")
  @interface DeleteImageOnStoreSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "상점 이미지 설정 변경 api",
      description = "상점에 등록된 이미지 설정 변경")
  @Parameters(value = {
      @Parameter(name = "storeId", description = "이미지를 변경할 상점의 id", in = ParameterIn.PATH),
      @Parameter(name = "imageId", description = "변경할 이미지의 id", in = ParameterIn.PATH)
  })
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = StoreImageDto.UpdateRequest.class)),
      description = "변경할 상점 이미지 정보")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "이미지 설정 변경에 성공한 경우"),
      @ApiResponse(
          responseCode = "404", description = "사업가, 상점, 이미지 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403", description = "로그인한 사업가가 상점에 대한 권한이 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Store Management Api")
  @interface UpdateStoreImageInformationSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "상점 삭제 api",
      description = "등록되어있는 상점을 삭제. 단, 확인하지 않거나 진행중인 주문이 있는 경우 삭제 불가")
  @Parameter(name = "storeId", description = "이미지를 변경할 상점의 id", in = ParameterIn.PATH)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204", description = "상점 삭제에 성공한 경우"),
      @ApiResponse(
          responseCode = "404", description = "사업가, 상점 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403", description = "로그인한 사업가가 상점에 대한 권한이 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "Entrepreneur Store Management Api")
  @interface DeleteStoreSwagger {}
}
