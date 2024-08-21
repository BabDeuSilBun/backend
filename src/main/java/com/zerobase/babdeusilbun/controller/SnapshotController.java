package com.zerobase.babdeusilbun.controller;

import static com.zerobase.babdeusilbun.dto.SnapshotDto.*;

import com.zerobase.babdeusilbun.dto.SnapshotDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.SnapshotService;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class SnapshotController {

  private final SnapshotService snapshotService;

  /**
   * 주문 후 공동 주문 스냅샷 리스트 조회
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/meetings/{meetingId}/snapshots/post-purchases/team")
  public ResponseEntity<Page<SubPurchaseSnapshot>> getTeamPurchaseSnapshots(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long meetingId, Pageable pageable
  ) {

    return ResponseEntity.ok(
        snapshotService.getTeamPurchaseSnapshots(userDetails.getId(), meetingId, pageable)
            .map(SubPurchaseSnapshot::fromSnapshotEntity)
    );
  }

  /**
   * 주문 후 개별 주문 스냅샷 리스트 조회
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/meetings/{meetingId}/snapshots/post-purchases/individuals")
  public ResponseEntity<Page<SubPurchaseSnapshot>> getIndividualPurchaseSnapshots(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long meetingId, Pageable pageable
  ) {

    return ResponseEntity.ok(
        snapshotService.getIndividualPurchaseSnapshots(userDetails.getId(), meetingId, pageable)
            .map(SubPurchaseSnapshot::fromSnapshotEntity)
    );
  }

  /**
   * 주문 후 주문 스냅샷 조회
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/meetings/{meetingId}/snapshots/post-purchases/purchases")
  public ResponseEntity<PurchaseSnapshot> getPurchaseSnapshots(
      @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long meetingId
  ) {

    return ResponseEntity.ok(PurchaseSnapshot.fromSnapshotEntity(
        snapshotService.getPurchaseSnapshot(userDetails.getId(), meetingId)
    ));
  }

  /**
   * 포인트 스냅샷 리스트 조회
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/snapshots/points")
  public ResponseEntity<Page<PointSnapshot>> getPointSnapshotList(
      @AuthenticationPrincipal CustomUserDetails userDetails, Pageable pageable) {

    return ResponseEntity.ok(
        snapshotService.getPointSnapshotList(userDetails.getId(), pageable).map(PointSnapshot::fromPointEntity)
    );
  }

  /**
   * 결제 스냅샷 조회
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/meetings/{meetingId}/snapshots/payments")
  public ResponseEntity<PaymentSnapshot> getPaymentSnapshots(
      @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long meetingId
  ) {

    return ResponseEntity.ok(PaymentSnapshot.fromPaymentEntity(
        snapshotService.getPaymentSnapshot(userDetails.getId(), meetingId)
    ));
  }


}





