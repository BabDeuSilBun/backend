package com.zerobase.babdeusilbun.controller.lookup;

import com.zerobase.babdeusilbun.dto.SchoolDto.Information;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.SchoolService;
import com.zerobase.babdeusilbun.swagger.annotation.lookup.UserLookUpSwagger.SearchCampusOfSameSchoolSwagger;
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
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserLookupController {
  private final SchoolService schoolService;

  @GetMapping("/campus")
  @SearchCampusOfSameSchoolSwagger
  public ResponseEntity<Page<Information>> searchCampusOfSameSchool(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestParam(name = "schoolId", required = false, defaultValue = "") Long schoolId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(schoolService.searchCampusBySchool(user, schoolId, page, size));
  }
}
