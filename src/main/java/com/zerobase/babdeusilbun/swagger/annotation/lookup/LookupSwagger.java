package com.zerobase.babdeusilbun.swagger.annotation.lookup;

import com.zerobase.babdeusilbun.dto.CategoryDto;
import com.zerobase.babdeusilbun.dto.MajorDto;
import com.zerobase.babdeusilbun.dto.SchoolDto;
import com.zerobase.babdeusilbun.dto.UserDto;
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

public @interface LookupSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "학교 검색 api",
      description = "학교와 캠퍼스 검색")
  @Parameters(value = {
      @Parameter(name = "schoolName", description = "검색어", in = ParameterIn.QUERY),
      @Parameter(name = "page", description = "조회하려는 페이지 번호(시작점: 0, 기본값: 0)", in = ParameterIn.QUERY),
      @Parameter(name = "size", description = "조회하려는 페이지의 요소 개수(최소:1, 기본값: 10)", in = ParameterIn.QUERY)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "학교 검색에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = SchoolDto.Information.class))))
  })
  @Tag(name = "User Lookup Api")
  @Tag(name = "Entrepreneur Lookup Api")
  @Tag(name = "Common Lookup Api")
  @interface SearchSchoolAndCampusSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "학과 검색 api",
      description = "전공 학과 검색")
  @Parameters(value = {
      @Parameter(name = "majorName", description = "검색어", in = ParameterIn.QUERY),
      @Parameter(name = "page", description = "조회하려는 페이지 번호(시작점: 0, 기본값: 0)", in = ParameterIn.QUERY),
      @Parameter(name = "size", description = "조회하려는 페이지의 요소 개수(최소:1, 기본값: 10)", in = ParameterIn.QUERY)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "학과 검색에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = MajorDto.Information.class))))
  })
  @Tag(name = "User Lookup Api")
  @Tag(name = "Entrepreneur Lookup Api")
  @Tag(name = "Common Lookup Api")
  @interface SearchMajorSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "카테고리 조회 api",
      description = "밥드실분에서 상점을 나누는 카테고리 목록 조회")
  @Parameters(value = {
      @Parameter(name = "page", description = "조회하려는 페이지 번호(시작점: 0, 기본값: 0)", in = ParameterIn.QUERY),
      @Parameter(name = "size", description = "조회하려는 페이지의 요소 개수(최소:1, 기본값: 10)", in = ParameterIn.QUERY)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "카테고리 조회에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = CategoryDto.Information.class))))
  })
  @Tag(name = "User Lookup Api")
  @Tag(name = "Entrepreneur Lookup Api")
  @Tag(name = "Common Lookup Api")
  @interface GetAllCategoriesSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "랜덤 닉네임 생성 api",
      description = "수식어 + 명사 + 랜덤 숫자로 랜덤 닉네임을 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "랜덤 닉네임 생성에 성공한 경우",
          content = @Content(schema = @Schema(implementation = UserDto.NicknameResponse.class)))
  })
  @Tag(name = "User Lookup Api")
  @Tag(name = "Entrepreneur Lookup Api")
  @Tag(name = "Common Lookup Api")
  @interface GetRandomNicknameSwagger {}
}
