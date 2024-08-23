package com.zerobase.babdeusilbun.controller;

import com.zerobase.babdeusilbun.domain.Point;
import com.zerobase.babdeusilbun.dto.PointDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/points")
@RequiredArgsConstructor
public class PointController {

  private final PointService pointService;

  /**
   * 포인트 내역 조회
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping
  public ResponseEntity<Page<PointDto.Response>> getAllPointList(
      @AuthenticationPrincipal CustomUserDetails userDetails, Pageable pageable, String sortCriteria
  ) {

    return ResponseEntity.ok(
        pointService.getAllPointList(userDetails.getId(), pageable, sortCriteria)
    );
  }

}
