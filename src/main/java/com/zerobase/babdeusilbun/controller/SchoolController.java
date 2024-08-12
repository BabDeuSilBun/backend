package com.zerobase.babdeusilbun.controller;

import com.zerobase.babdeusilbun.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SchoolController {
  private final SchoolService schoolService;

  /**
   * 학교 검색(캠퍼스 포함)
   */
  @GetMapping("/signup/schools")
  public ResponseEntity<?> searchSchoolAndCampus(
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size,
      @RequestParam(name = "schoolName", required = false, defaultValue = "") String schoolName) {
    return ResponseEntity.ok(schoolService.searchSchoolAndCampus(page, size, schoolName));
  }
}
