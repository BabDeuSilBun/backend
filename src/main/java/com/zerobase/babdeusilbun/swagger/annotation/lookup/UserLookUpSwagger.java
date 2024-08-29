package com.zerobase.babdeusilbun.swagger.annotation.lookup;

import com.zerobase.babdeusilbun.dto.SchoolDto.Information;
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

public @interface UserLookUpSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "동일 학교 캠퍼스 목록 조회 api",
      description = "동일 학교 캠퍼스 목록을 조회. 단, schoolId를 입력하지 않은 경우 로그인한 이용자의 학교로 자동 지정")
  @Parameters(value = {
      @Parameter(name = "schoolId", description = "조회하려는 학교의 id"),
      @Parameter(name = "page", description = "조회하려는 페이지 번호(시작점: 0, 기본값: 0)"),
      @Parameter(name = "size", description = "조회하려는 페이지의 요소 개수(최소:1, 기본값: 10)")
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "캠퍼스 조회에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = Information.class))))
  })
  @Tag(name = "User Lookup Api")
  @interface SearchCampusOfSameSchoolSwagger {}

}
