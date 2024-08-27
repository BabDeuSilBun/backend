package com.zerobase.babdeusilbun.controller;

import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.IndividualPurchaseService;
import com.zerobase.babdeusilbun.swagger.annotation.IndividualPurchaseSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.zerobase.babdeusilbun.dto.IndividualPurchaseDto.*;
import static com.zerobase.babdeusilbun.swagger.annotation.IndividualPurchaseSwagger.*;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class IndividualPurchaseController {

    private final IndividualPurchaseService individualPurchaseService;

    /**
     * 개별주문 장바구니 등록
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/meetings/{meetingId}/individual-purchases")
    @CreateIndividualPurchaseSwagger
    public ResponseEntity<Void> createIndividualPurchase(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long meetingId,
            @Validated @RequestBody CreateRequest request) {
        individualPurchaseService.createIndividualPurchase(userDetails.getId(), meetingId, request);

        return ResponseEntity.status(CREATED).build();
    }

    /**
     * 개별주문 장바구니 수정
     */
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/individual-purchases/{purchaseId}")
    @UpdateIndividualPurchaseSwagger
    public ResponseEntity<Void> updateIndividualPurchase(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long purchaseId,
            @Validated @RequestBody UpdateRequest request) {

        individualPurchaseService.updateIndividualPurchase(userDetails.getId(), purchaseId, request);

        return ResponseEntity.ok().build();
    }

    /**
     * 개별주문 장바구니 삭제
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/individual-purchases/{purchaseId}")
    @DeleteIndividualPurchaseSwagger
    public ResponseEntity<Void> deleteIndividualPurchase(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long purchaseId) {
        individualPurchaseService.deleteIndividualPurchase(userDetails.getId(), purchaseId);

        return ResponseEntity.ok().build();
    }
}
