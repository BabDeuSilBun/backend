package com.zerobase.babdeusilbun.controller;

import static com.zerobase.babdeusilbun.swagger.annotation.SchoolSwagger.SearchCampusBySchoolSwagger;
import static com.zerobase.babdeusilbun.swagger.annotation.SchoolSwagger.SearchSchoolAndCampusSwagger;

import com.zerobase.babdeusilbun.dto.SchoolDto.Information;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
   * 학교 검색 (캠퍼스 포함)
   */
  @GetMapping("/schools")
  @SearchSchoolAndCampusSwagger
  public ResponseEntity<Page<Information>> searchSchoolAndCampus(
      @RequestParam(required = false, defaultValue = "") String schoolName,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(schoolService.searchSchoolAndCampus(schoolName, page, size));
  }


  /**
   * 캠퍼스 검색 (입력값에 schoolId가 있는 경우 해당 id 기준, 없는 경우 로그인한 이용자 기준)
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/campus")
  @SearchCampusBySchoolSwagger
  public ResponseEntity<Page<Information>> searchCampusBySchool(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestParam(required = false, defaultValue = "") Long schoolId,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(schoolService.searchCampusBySchool(user, schoolId, page, size));
  }
}
