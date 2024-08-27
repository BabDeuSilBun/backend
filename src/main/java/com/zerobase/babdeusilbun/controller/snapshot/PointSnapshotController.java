package com.zerobase.babdeusilbun.controller.snapshot;

import static com.zerobase.babdeusilbun.dto.SnapshotDto.PointSnapshot;
import static com.zerobase.babdeusilbun.swagger.annotation.snapshot.PointSnapshotSwagger.*;

import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.SnapshotService;
import com.zerobase.babdeusilbun.swagger.annotation.snapshot.PointSnapshotSwagger;
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
@RequestMapping("/api/users/snapshots/points")
@RequiredArgsConstructor
public class PointSnapshotController {

  private final SnapshotService snapshotService;

  /**
   * 포인트 스냅샷 리스트 조회
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping
  @GetPointSnapshotListSwagger
  public ResponseEntity<Page<PointSnapshot>> getPointSnapshotList(
      @AuthenticationPrincipal CustomUserDetails userDetails, Pageable pageable) {

    return ResponseEntity.ok(
        snapshotService.getPointSnapshotList(userDetails.getId(), pageable).map(PointSnapshot::fromPointEntity)
    );
  }

}





