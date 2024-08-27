package com.zerobase.babdeusilbun.swagger.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface Swagger {

  /**
   * @Target(ElementType.METHOD)
   *     @Retention(RetentionPolicy.RUNTIME)
   *     @Inherited
   *     @Operation(summary = "상품 정보 조회 API", description = "상품 정보를 조회하는 API")
   *     @Parameter(name = "organizationCode", description = "기관 코드", schema = @Schema(implementation = Organization.class), required = true)
   *     @ApiResponse(responseCode = "200", content = @Content(examples = @ExampleObject(value = """
   *             {
   *                 "data": [
   *                     {
   *                         "organizationCode": "00001",
   *                         "productCode": "001",
   *                         "productMaximumInterest": 9.9,
   *                         "productMinimumInterest": 1.1,
   *                         "productName": "상품 1"
   *                     }
   *                 ],
   *                  "responseCode": "00",
   *                  "responseMessage": "success"
   *             }
   *             """)))
   *     @interface GetProductInfo {
   *         // com.project.loanservice.controller.impl.ProductControllerImpl
   *     }
   */

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "학교 검색", description = "캠퍼스 포함")
  @Parameter(name = "schoolName")
  @Tag(name = "school")
  @interface searchSchoolAndCampus {}

}
