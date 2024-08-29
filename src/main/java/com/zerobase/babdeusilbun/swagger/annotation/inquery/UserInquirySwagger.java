package com.zerobase.babdeusilbun.swagger.annotation.inquery;

import com.zerobase.babdeusilbun.dto.InquiryDto;
import com.zerobase.babdeusilbun.dto.InquiryDto.Response;
import com.zerobase.babdeusilbun.dto.InquiryImageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

public @interface UserInquirySwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "문의 게시물 목록 조회 api",
      description = "본인이 작성한 문의 게시물 목록 조회")
  @Parameter(name = "pageable", description = "문의 게시물 목록에서 보일 페이지번호와 한 페이지당 보이는 항목 개수")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "문의 게시물 목록 조회에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = Response.class))))
  })
  @Tag(name = "User Inquiry Api")
  @interface GetInquiryListSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "문의 게시글 작성 api",
      description = "문의 게시글 작성")
  @Parameter(
      name = "files", description = "최대 3장, 10MB 이하",
      content = @Content(mediaType = "multipart/form-data",
      array = @ArraySchema(schema = @Schema(implementation = MultipartFile.class))))
  @RequestBody(
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = InquiryDto.Request.class)),
      description = "작성할 게시글의 제목과 내용")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "게시글 작성에 성공한 경우")
  })
  @Tag(name = "User Inquiry Api")
  @interface CreateInquirySwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "문의 이미지 전체 조회 api",
      description = "문의 게시글에 등록된 문의 이미지 전체 조회")
  @Parameter(name = "inquiryId", description = "조회하려는 이미지가 등록된 게시글 id", in = ParameterIn.PATH)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "이미지 전체 조회에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = InquiryImageDto.class))))
  })
  @Tag(name = "User Inquiry Api")
  @interface GetInquiryImagesSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "문의 이미지 순서 변경 api",
      description = "문의 게시글에 등록된 문의 이미지의 순서 변경")
  @Parameters(value = {
      @Parameter(name = "inquiryId", description = "순서를 변경하려는 이미지가 등록된 게시글의 id 값", in = ParameterIn.PATH),
      @Parameter(name = "imageId", description = "순서를 변경하려는 이미지의 id 값", in = ParameterIn.PATH),
      @Parameter(name = "sequence", description = "변경할 순서 값")
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "순서 변경에 성공한 경우")
  })
  @Tag(name = "User Inquiry Api")
  @interface UpdateInquiryImageSequenceSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "문의 이미지 삭제",
      description = "문의 게시글에 등록된 문의 이미지 삭제")
  @Parameters(value = {
      @Parameter(name = "inquiryId", description = "삭제하려는 이미지가 등록된 게시글의 id 값", in = ParameterIn.PATH),
      @Parameter(name = "imageId", description = "삭제하려는 이미지의 id 값", in = ParameterIn.PATH)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "이미지 삭제에 성공한 경우")
  })
  @Tag(name = "User Inquiry Api")
  @interface DeleteInquiryImageSwagger {}
}
