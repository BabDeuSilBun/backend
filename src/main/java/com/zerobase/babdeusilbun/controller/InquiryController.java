package com.zerobase.babdeusilbun.controller;

import static com.zerobase.babdeusilbun.swagger.annotation.InquirySwagger.*;
import static org.springframework.http.HttpStatus.*;

import com.zerobase.babdeusilbun.dto.InquiryDto;
import com.zerobase.babdeusilbun.dto.InquiryDto.Response;
import com.zerobase.babdeusilbun.dto.InquiryImageDto;
import com.zerobase.babdeusilbun.service.InquiryService;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.swagger.annotation.InquirySwagger;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
@RequiredArgsConstructor
public class InquiryController {


  private final InquiryService inquiryService;

  /**
   * 문의 게시물 목록 조회
   */
  @GetMapping
  @GetInquiryListSwagger
  public ResponseEntity<Page<Response>> getInquiryList(
      @AuthenticationPrincipal CustomUserDetails userDetails, Pageable pageable
  ) {

    return ResponseEntity.ok(
        inquiryService.getInquiryList(userDetails.getId(), pageable)
            .map(InquiryDto.Response::fromEntity)
    );
  }

  /**
   * 문의 게시글 작성
   */
  @PostMapping
  @CreateInquirySwagger
  public ResponseEntity<Void> createInquiry(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Validated @RequestPart("request") InquiryDto.Request request,
      @RequestPart(value = "files", required = false) List<MultipartFile> images
  ) {

    // TODO
    //  userId로 변경
    inquiryService.createInquiry(userDetails, request, images);

    return ResponseEntity.status(CREATED).build();
  }

  /**
   * 문의 이미지 전체 조회
   */
  @GetMapping("/{inquiryId}/images")
  @GetInquiryImagesSwagger
  public ResponseEntity<List<InquiryImageDto>> getInquiryImages(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long inquiryId
  ) {

    return ResponseEntity.ok(
        inquiryService.getInquiryImageList(userDetails.getId(), inquiryId)
            .stream().map(InquiryImageDto::fromEntity)
            .toList()
    );
  }

  /**
   * 문의 이미지 순서 변경
   */
  @PatchMapping("/{inquiryId}/images/{imageId}")
  @UpdateInquiryImageSequenceSwagger
  public ResponseEntity<Void> updateInquiryImageSequence(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long inquiryId, @PathVariable Long imageId,
      @RequestParam Integer sequence
  ) {

    // TODO
    //  userId로 변경
    inquiryService.updateImageSequence(userDetails, inquiryId, imageId, sequence);

    return ResponseEntity.status(OK).build();
  }


  /**
   * 문의 이미지 삭제
   */
  @DeleteMapping("/{inquiryId}/images/{imageId}")
  @DeleteInquiryImageSwagger
  public ResponseEntity<Void> deleteInquiryImage(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long inquiryId, @PathVariable Long imageId
  ) {

    // TODO
    //  userId로 변경
    inquiryService.deleteImage(userDetails, inquiryId, imageId);

    return ResponseEntity.status(OK).build();
  }

}
