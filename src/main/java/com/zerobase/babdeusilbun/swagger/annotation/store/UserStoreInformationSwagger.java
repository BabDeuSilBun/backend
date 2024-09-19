package com.zerobase.babdeusilbun.swagger.annotation.store;

import com.zerobase.babdeusilbun.dto.StoreDto.Information;
import com.zerobase.babdeusilbun.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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

public @interface UserStoreInformationSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "주문 가능 가게 리스트 검색/조회 api", 
      description = "설정된 학교 기준으로 주문 가능한 가게 목록 검색/조회")
  @Parameters(value = {
      @Parameter(name = "foodCategoryFilter", description = "살펴볼 카테고리 목록 지정"),
      @Parameter(name = "searchMenu", description = "검색어"),
      @Parameter(name = "schoolId", description = "배달 가능한지 알아볼 기준이 될 학교 id"),
      @Parameter(name = "sortCriteria", description = "정렬 기준")
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "주문 가능 가게 리스트 검색/조회에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = Information.class)))),
      @ApiResponse(
          responseCode = "404", description = "학교 아이디가 없고, 로그인한 이용자의 정보를 찾을 수 없는 경우",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Tag(name = "User Store Information Api")
  @interface GetAvailStoreListSwagger {}
}
