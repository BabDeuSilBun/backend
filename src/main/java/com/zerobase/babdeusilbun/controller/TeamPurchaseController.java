package com.zerobase.babdeusilbun.controller;

import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.TeamPurchaseService;
import com.zerobase.babdeusilbun.swagger.annotation.TeamPurchaseSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.zerobase.babdeusilbun.dto.TeamPurchaseDto.*;
import static com.zerobase.babdeusilbun.swagger.annotation.TeamPurchaseSwagger.*;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class TeamPurchaseController {

    private final TeamPurchaseService teamPurchaseService;

    /**
     * 공통주문 장바구니 등록
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/meetings/{meetingId}/team-purchases")
    @CreateTeamPurchaseSwagger
    public ResponseEntity<Void> createTeamPurchase(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long meetingId,
            @Validated @RequestBody CreateRequest request) {
        teamPurchaseService.createTeamPurchase(userDetails.getId(), meetingId, request);

        return ResponseEntity.status(CREATED).build();
    }

    /**
     * 공통주문 장바구니 수정
     */
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/team-purchases/{purchaseId}")
    @UpdateTeamPurchaseSwagger
    public ResponseEntity<Void> updateTeamPurchase(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long purchaseId,
            @Validated @RequestBody UpdateRequest request) {

        teamPurchaseService.updateTeamPurchase(userDetails.getId(), purchaseId, request);

        return ResponseEntity.ok().build();
    }

    /**
     * 공통주문 장바구니 삭제
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/team-purchases/{purchaseId}")
    @DeleteTeamPurchaseSwagger
    public ResponseEntity<Void> deleteTeamPurchase(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long purchaseId) {
        teamPurchaseService.deleteTeamPurchase(userDetails.getId(), purchaseId);

        return ResponseEntity.ok().build();
    }
}
