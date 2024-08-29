package com.zerobase.babdeusilbun.swagger.annotation.store;

import com.zerobase.babdeusilbun.dto.EntrepreneurDto;
import com.zerobase.babdeusilbun.dto.HolidayDto;
import com.zerobase.babdeusilbun.dto.MenuDto.Information;
import com.zerobase.babdeusilbun.dto.StoreCategoryDto;
import com.zerobase.babdeusilbun.dto.StoreDto.PrincipalInformation;
import com.zerobase.babdeusilbun.dto.StoreImageDto;
import com.zerobase.babdeusilbun.dto.StoreSchoolDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

public @interface StoreInformationSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "상점 정보 조회 api", description = "상점 기본 정보 조회")
  @Parameter(name = "storeId", description = "확인하려는 상점의 id", in = ParameterIn.PATH)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "상점 기본 정보 조회에 성공한 경우",
          content = @Content(schema = @Schema(implementation = PrincipalInformation.class)))
  })
  @Tag(name = "Entrepreneur Store Information Api")
  @Tag(name = "User Store Information Api")
  @Tag(name = "Common Store Information Api")
  @interface GetStoreInfoSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "상점 휴무일 조회 api",
      description = "상점의 휴무일로 등록된 요일 목록 조회")
  @Parameters(value = {
      @Parameter(name = "storeId", description = "확인하려는 상점 id", in = ParameterIn.PATH),
      @Parameter(name = "page", description = "목록 페이지 번호(시작점: 0, 기본값: 0)", in = ParameterIn.QUERY),
      @Parameter(name = "size", description = "목록 페이지의 요소 개수(최소:1, 기본값: 10)", in = ParameterIn.QUERY)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "상점의 휴무일 목록 조회에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = HolidayDto.Information.class))))
  })
  @Tag(name = "Entrepreneur Store Information Api")
  @Tag(name = "User Store Information Api")
  @Tag(name = "Common Store Information Api")
  @interface GetAllHolidaysSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "상점 메뉴 목록 조회 api",
      description = "상점에 등록된 메뉴 목록 조회")
  @Parameters(value = {
      @Parameter(name = "storeId", description = "확인하려는 상점 id", in = ParameterIn.PATH),
      @Parameter(name = "page", description = "목록 페이지 번호(시작점: 0, 기본값: 0)", in = ParameterIn.QUERY),
      @Parameter(name = "size", description = "목록 페이지의 요소 개수(최소:1, 기본값: 10)", in = ParameterIn.QUERY)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "상점 메뉴 목록 조회에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = Information.class))))
  })
  @Tag(name = "Entrepreneur Store Information Api")
  @Tag(name = "User Store Information Api")
  @Tag(name = "Common Store Information Api")
  @interface GetAllMenuSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "상점 사업자 정보 조회 api", description = "상점을 관리하는 사업자 정보 조회")
  @Parameter(name = "storeId", description = "확인하려는 상점 id", in = ParameterIn.PATH)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "사업자 정보 조회에 성공한 경우",
          content = @Content(schema = @Schema(implementation = EntrepreneurDto.SimpleInformation.class)))
  })
  @Tag(name = "Entrepreneur Store Information Api")
  @Tag(name = "User Store Information Api")
  @Tag(name = "Common Store Information Api")
  @interface GetEntrepreneurSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "상점 이미지 전체 조회 api",
      description = "상점에 등록된 모든 이미지 조회")
  @Parameters(value = {
      @Parameter(name = "storeId", description = "확인하려는 상점 id", in = ParameterIn.PATH),
      @Parameter(name = "page", description = "목록 페이지 번호(시작점: 0, 기본값: 0)", in = ParameterIn.QUERY),
      @Parameter(name = "size", description = "목록 페이지의 요소 개수(최소:1, 기본값: 10)", in = ParameterIn.QUERY)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "상점 이미지 전체 조회에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = StoreImageDto.Information.class))))
  })
  @Tag(name = "Entrepreneur Store Information Api")
  @Tag(name = "User Store Information Api")
  @Tag(name = "Common Store Information Api")
  @interface GetAllImagesSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "썸네일 조회 api", description = "상점에 등록된 이미지들 중 대표 이미지로 설정된 이미지 조회")
  @Parameter(name = "storeId", description = "확인하려는 상점 id", in = ParameterIn.PATH)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "썸네일 조회에 성공한 경우",
          content = @Content(schema = @Schema(implementation = StoreImageDto.Thumbnail.class)))
  })
  @Tag(name = "Entrepreneur Store Information Api")
  @Tag(name = "User Store Information Api")
  @Tag(name = "Common Store Information Api")
  @interface GetThumbnailSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "상점 카테고리 목록 조회 api",
      description = "상점에 등록된 카테고리 목록 전체 조회")
  @Parameters(value = {
      @Parameter(name = "storeId", description = "확인하려는 상점 id", in = ParameterIn.PATH),
      @Parameter(name = "page", description = "목록 페이지 번호(시작점: 0, 기본값: 0)", in = ParameterIn.QUERY),
      @Parameter(name = "size", description = "목록 페이지의 요소 개수(최소:1, 기본값: 10)", in = ParameterIn.QUERY)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "상점 카테고리 목록 조회에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = StoreCategoryDto.Information.class))))
  })
  @Tag(name = "Entrepreneur Store Information Api")
  @Tag(name = "User Store Information Api")
  @Tag(name = "Common Store Information Api")
  @interface GetAllCategoriesByStoreSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "상점 배달 가능 캠퍼스 목록 조회 api",
      description = "상점이 배달 가능한 캠퍼스로 등록한 캠퍼스 목록 전체 조회")
  @Parameters(value = {
      @Parameter(name = "storeId", description = "확인하려는 상점 id", in = ParameterIn.PATH),
      @Parameter(name = "page", description = "목록 페이지 번호(시작점: 0, 기본값: 0)", in = ParameterIn.QUERY),
      @Parameter(name = "size", description = "목록 페이지의 요소 개수(최소:1, 기본값: 10)", in = ParameterIn.QUERY)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "상점 배달 가능 캠퍼스 목록 조회에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = StoreSchoolDto.Information.class))))
  })
  @Tag(name = "Entrepreneur Store Information Api")
  @Tag(name = "User Store Information Api")
  @Tag(name = "Common Store Information Api")
  @interface GetAllSchoolsSwagger {}
}
