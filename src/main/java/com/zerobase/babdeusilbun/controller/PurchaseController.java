package com.zerobase.babdeusilbun.controller;

import com.zerobase.babdeusilbun.dto.PurchaseDto;
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

  @PreAuthorize("hasRole('USER')")
  @GetMapping("/team-order")
  public ResponseEntity<PurchaseDto.TeamPurchaseResponse> getTeamPurchaseCart(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long meetingId, Pageable pageable
  ) {

    return ResponseEntity.ok(
        purchaseService.getTeamOrderCart(userDetails.getId(), meetingId, pageable));

  }

}
