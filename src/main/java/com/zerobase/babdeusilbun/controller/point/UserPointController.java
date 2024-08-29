package com.zerobase.babdeusilbun.controller.point;

import static com.zerobase.babdeusilbun.dto.SnapshotDto.PointSnapshot;

import com.zerobase.babdeusilbun.dto.PointDto.Response;
import com.zerobase.babdeusilbun.dto.PointDto.WithdrawalRequest;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.PointService;
import com.zerobase.babdeusilbun.service.SnapshotService;
import com.zerobase.babdeusilbun.swagger.annotation.point.UserPointSwagger.GetAllPointListSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.point.UserPointSwagger.GetPointSnapshotListSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.point.UserPointSwagger.WithdrawalPointSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserPointController {
  private final SnapshotService snapshotService;
  private final PointService pointService;

  @GetMapping("/points")
  @GetAllPointListSwagger
  public ResponseEntity<Page<Response>> getAllPointList(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestParam(name="sortCriteria", required = false) String sortCriteria,
      @RequestParam(name = "pageable") Pageable pageable
  ) {

    return ResponseEntity.ok(
        pointService.getAllPointList(userDetails.getId(), pageable, sortCriteria)
    );
  }

  @PostMapping("/points/withdrawal")
  @WithdrawalPointSwagger
  public ResponseEntity<Void> withdrawalPoint(
      @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody WithdrawalRequest request
  ) {

    pointService.withdrawalPoint(userDetails.getId(), request);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/snapshots/points")
  @GetPointSnapshotListSwagger
  public ResponseEntity<Page<PointSnapshot>> getPointSnapshotList(
      @AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam("pageable") Pageable pageable) {

    return ResponseEntity.ok(
        snapshotService.getPointSnapshotList(userDetails.getId(), pageable).map(PointSnapshot::fromPointEntity)
    );
  }
}





