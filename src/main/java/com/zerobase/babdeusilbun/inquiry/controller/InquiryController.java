package com.zerobase.babdeusilbun.inquiry.controller;

import com.zerobase.babdeusilbun.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/inquiries")
@RequiredArgsConstructor
public class InquiryController {


  private final InquiryService inquiryService;

  // 문의 게시물 목록 조회
  @GetMapping
  public ResponseEntity<?> getInquiryList() {

  }

  // 문의 게시물 상세 조회 /{inquiryId}

  // 문의 게시글 작성

  // 문의 이미지 전체 조회 /images

  // 문의 이미지 순서 변경  /images/{imageId}

  // 문의 이미지 삭제  /images/{imageId}

}
