package com.zerobase.babdeusilbun.controller.inquery;

import static com.zerobase.babdeusilbun.swagger.annotation.inquery.UserInquirySwagger.CreateInquirySwagger;
import static com.zerobase.babdeusilbun.swagger.annotation.inquery.UserInquirySwagger.DeleteInquiryImageSwagger;
import static com.zerobase.babdeusilbun.swagger.annotation.inquery.UserInquirySwagger.GetInquiryImagesSwagger;
import static com.zerobase.babdeusilbun.swagger.annotation.inquery.UserInquirySwagger.GetInquiryListSwagger;
import static com.zerobase.babdeusilbun.swagger.annotation.inquery.UserInquirySwagger.UpdateInquiryImageSequenceSwagger;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.zerobase.babdeusilbun.dto.InquiryDto;
import com.zerobase.babdeusilbun.dto.InquiryDto.Response;
import com.zerobase.babdeusilbun.dto.InquiryImageDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.InquiryService;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users/inquiries")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
@Slf4j
public class UserInquiryController {
  private final InquiryService inquiryService;

  @GetMapping
  @GetInquiryListSwagger
  public ResponseEntity<Page<Response>> getInquiryList(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Parameter(description = "문의 게시물 목록에서 보일 페이지번호와 한 페이지당 보이는 항목 개수")
      Pageable pageable
  ) {
    return ResponseEntity.ok(
        inquiryService.getInquiryList(userDetails.getId(), pageable)
            .map(InquiryDto.Response::fromEntity)
    );
  }

  @PostMapping(consumes = {MULTIPART_FORM_DATA_VALUE, APPLICATION_JSON_VALUE})
  @CreateInquirySwagger
  public ResponseEntity<Void> createInquiry(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Parameter(description = "작성할 게시글의 제목과 내용")
      @Validated @RequestPart("request") InquiryDto.Request request,
      @Parameter(description = "최대 3장, 10MB 이하")
      @RequestPart(value = "file", required = false) List<MultipartFile> images
  ) {

    log.info("[Create Inquiry][{}]", userDetails.getEmail());
    log.info("[request][{}]", request.toString());

    if (images != null) {

      for (MultipartFile image : images) {
        log.info("[images][{}]", image.getName() + ":" + image.getContentType() + ":" + image.getSize());
      }
    } else {
      log.info("image is null");
    }

    inquiryService.createInquiry(userDetails.getId(), request, images);

    return ResponseEntity.status(CREATED).build();
  }

  @GetMapping("/{inquiryId}/images")
  @GetInquiryImagesSwagger
  public ResponseEntity<List<InquiryImageDto>> getInquiryImages(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable("inquiryId") Long inquiryId
  ) {

    return ResponseEntity.ok(
        inquiryService.getInquiryImageList(userDetails.getId(), inquiryId)
            .stream().map(InquiryImageDto::fromEntity)
            .toList()
    );
  }

  @PatchMapping("/{inquiryId}/images/{imageId}")
  @UpdateInquiryImageSequenceSwagger
  public ResponseEntity<Void> updateInquiryImageSequence(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable("inquiryId") Long inquiryId, @PathVariable("imageId") Long imageId,
      @RequestParam("sequence") Integer sequence
  ) {

    inquiryService.updateImageSequence(userDetails.getId(), inquiryId, imageId, sequence);

    return ResponseEntity.status(OK).build();
  }


  /**
   * 문의 이미지 삭제
   */
  @DeleteMapping("/{inquiryId}/images/{imageId}")
  @DeleteInquiryImageSwagger
  public ResponseEntity<Void> deleteInquiryImage(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable("inquiryId") Long inquiryId, @PathVariable("imageId") Long imageId
  ) {

    inquiryService.deleteImage(userDetails.getId(), inquiryId, imageId);

    return ResponseEntity.status(OK).build();
  }
}
