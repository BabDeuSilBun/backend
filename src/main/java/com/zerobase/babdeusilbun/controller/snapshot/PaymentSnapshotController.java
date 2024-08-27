package com.zerobase.babdeusilbun.controller.snapshot;

import static com.zerobase.babdeusilbun.dto.SnapshotDto.PaymentSnapshot;
import static com.zerobase.babdeusilbun.swagger.annotation.snapshot.PaymentSnapshotSwagger.*;

import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.SnapshotService;
import com.zerobase.babdeusilbun.swagger.annotation.snapshot.PaymentSnapshotSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/meetings/{meetingId}/snapshots/payments")
@RequiredArgsConstructor
public class PaymentSnapshotController {

  private final SnapshotService snapshotService;

  /**
   * 결제 스냅샷 조회
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping
  @GetPaymentSnapshotsSwagger
  public ResponseEntity<PaymentSnapshot> getPaymentSnapshots(
      @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long meetingId
  ) {

    return ResponseEntity.ok(PaymentSnapshot.fromPaymentEntity(
        snapshotService.getPaymentSnapshot(userDetails.getId(), meetingId)
    ));
  }


}





