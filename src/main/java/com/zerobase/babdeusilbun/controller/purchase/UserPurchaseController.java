package com.zerobase.babdeusilbun.controller.purchase;

import static com.zerobase.babdeusilbun.dto.SnapshotDto.PurchaseSnapshot;
import static com.zerobase.babdeusilbun.dto.SnapshotDto.SubPurchaseSnapshot;

import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.SnapshotService;
import com.zerobase.babdeusilbun.swagger.annotation.purchase.UserPurchaseSwagger.GetIndividualPurchaseSnapshotsSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.purchase.UserPurchaseSwagger.GetPurchaseSnapshotsSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.purchase.UserPurchaseSwagger.GetTeamPurchaseSnapshotsSwagger;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/meetings/{meetingId}")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserPurchaseController {
  private final SnapshotService snapshotService;

  /**
   * 주문 후 공동 주문 스냅샷 리스트 조회
   */
  @GetMapping("/snapshots/post-purchases/team")
  @GetTeamPurchaseSnapshotsSwagger
  public ResponseEntity<Page<SubPurchaseSnapshot>> getTeamPurchaseSnapshots(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable("meetingId") Long meetingId,
      @Parameter(description = "스냅샷(영수증) 목록 페이지 넘버와 한 페이지당 보이는 항목 개수 설정")
      Pageable pageable
  ) {
    return ResponseEntity.ok(
        snapshotService.getTeamPurchaseSnapshots(userDetails.getId(), meetingId, pageable)
            .map(SubPurchaseSnapshot::fromSnapshotEntity)
    );
  }

  /**
   * 주문 후 개별 주문 스냅샷 리스트 조회
   */
  @GetMapping("/snapshots/post-purchases/individuals")
  @GetIndividualPurchaseSnapshotsSwagger
  public ResponseEntity<Page<SubPurchaseSnapshot>> getIndividualPurchaseSnapshots(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable("meetingId") Long meetingId,
      @Parameter(description = "스냅샷(영수증) 목록 페이지 넘버와 한 페이지당 보이는 항목 개수 설정")
      Pageable pageable
  ) {

    return ResponseEntity.ok(
        snapshotService.getIndividualPurchaseSnapshots(userDetails.getId(), meetingId, pageable)
            .map(SubPurchaseSnapshot::fromSnapshotEntity)
    );
  }

  /**
   * 주문 후 주문 스냅샷 조회
   */
  @GetMapping("/snapshots/post-purchases/purchases")
  @GetPurchaseSnapshotsSwagger
  public ResponseEntity<PurchaseSnapshot> getPurchaseSnapshots(
      @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("meetingId") Long meetingId
  ) {

    return ResponseEntity.ok(PurchaseSnapshot.fromSnapshotEntity(
        snapshotService.getPurchaseSnapshot(userDetails.getId(), meetingId)
    ));
  }
}





