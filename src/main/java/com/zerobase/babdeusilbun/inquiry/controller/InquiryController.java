package com.zerobase.babdeusilbun.inquiry.controller;

import static org.springframework.http.HttpStatus.*;

import com.zerobase.babdeusilbun.inquiry.dto.InquiryDto;
import com.zerobase.babdeusilbun.inquiry.dto.InquiryDto.ListResponse;
import com.zerobase.babdeusilbun.inquiry.dto.InquiryImageDto;
import com.zerobase.babdeusilbun.inquiry.service.InquiryService;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  // 문의 게시물 목록 조회
  @GetMapping
  public ResponseEntity<Page<ListResponse>> getInquiryList(
      @RequestParam String statusFilter, Pageable pageable
  ) {

    return ResponseEntity.ok(inquiryService.getInquiryList(statusFilter, pageable));
  }

  // 문의 게시물 상세 조회
  @GetMapping("/{inquiryId}")
  public ResponseEntity<InquiryDto.DetailResponse> getInquiryInfo(@PathVariable Long inquiryId) {

    return ResponseEntity.ok(inquiryService.getInquiryInfo(inquiryId));
  }

  // 문의 게시글 작성
  @PostMapping
  public ResponseEntity<Void> createInquiry(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Validated @RequestPart("request") InquiryDto.Request request,
      @RequestPart(value = "files", required = false) List<MultipartFile> images
  ) {

    inquiryService.createInquiry(userDetails, request, images);

    return ResponseEntity.status(CREATED).build();
  }

  // 문의 이미지 전체 조회
  @GetMapping("/{inquiryId}/images")
  public ResponseEntity<Page<InquiryImageDto>> getInquiryImages(@PathVariable Long inquiryId, Pageable pageable) {

    return ResponseEntity.ok(inquiryService.getInquiryImageList(inquiryId, pageable));
  }

  // 문의 이미지 순서 변경
  @PatchMapping("/{inquiryId}/images/{imageId}")
  public ResponseEntity<Void> updateInquiryImageSequence(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long inquiryId, @PathVariable Long imageId,
      @RequestParam Integer sequence
  ) {

    inquiryService.updateImageSequence(userDetails, inquiryId, imageId, sequence);

    return ResponseEntity.status(OK).build();
  }


  // 문의 이미지 삭제
  @DeleteMapping("/{inquiryId}/images/{imageId}")
  public ResponseEntity<Void> deleteInquiryImage(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long inquiryId, @PathVariable Long imageId
  ) {

    inquiryService.deleteImage(userDetails, inquiryId, imageId);

    return ResponseEntity.status(OK).build();
  }

}
