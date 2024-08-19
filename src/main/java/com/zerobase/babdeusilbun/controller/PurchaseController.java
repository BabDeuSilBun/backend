package com.zerobase.babdeusilbun.controller;

import static com.zerobase.babdeusilbun.dto.PurchaseDto.*;

import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.PurchaseService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PurchaseController {

  private final PurchaseService purchaseService;

  /**
   * 주문 전 공동 주문 장바구니 조회
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/team-order")
  public ResponseEntity<PurchaseResponse> getTeamPurchaseCart(
      @PathVariable Long meetingId, Pageable pageable
  ) {

    return ResponseEntity.ok(
        purchaseService.getTeamPurchaseCart(meetingId, pageable));
  }

  /**
   * 주문 전 개별 주문 장바구니 조회
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/individual-order")
  public ResponseEntity<PurchaseResponse> getIndividualPurchaseCart(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long meetingId, Pageable pageable
  ) {

    return ResponseEntity.ok(
        purchaseService.getIndividualPurchaseCart(userDetails.getId(), meetingId, pageable));
  }

  /**
   * 주문 전 모임 배달비 조회
   */
//  @PreAuthorize("hasRole('USER')")


}
