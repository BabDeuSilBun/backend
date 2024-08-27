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

public @interface InquirySwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "문의 게시물 목록 조회")
  @Parameters(value = {
      @Parameter(name = "userDetails"),
      @Parameter(name = "pageable")
  })
  @Tag(name = "Inquiry")
  @interface GetInquiryListSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "문의 게시글 작성")
  @Parameters(value = {
      @Parameter(name = "userDetails"),
  })
  @Tag(name = "Inquiry")
  @interface CreateInquirySwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "문의 이미지 전체 조회")
  @Parameters(value = {
      @Parameter(name = "userDetails"),
      @Parameter(name = "inquiryId")
  })
  @Tag(name = "Inquiry")
  @interface GetInquiryImagesSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "문의 이미지 순서 변경")
  @Parameters(value = {
      @Parameter(name = "userDetails"),
      @Parameter(name = "inquiryId"),
      @Parameter(name = "imageId"),
      @Parameter(name = "sequence")
  })
  @Tag(name = "Inquiry")
  @interface UpdateInquiryImageSequenceSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "문의 이미지 삭제")
  @Parameters(value = {
      @Parameter(name = "userDetails"),
      @Parameter(name = "inquiryId"),
      @Parameter(name = "imageId")
  })
  @Tag(name = "Inquiry")
  @interface DeleteInquiryImageSwagger {}

}
