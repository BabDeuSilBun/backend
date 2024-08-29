package com.zerobase.babdeusilbun.controller.cart;

import static com.zerobase.babdeusilbun.dto.TeamPurchaseDto.CreateRequest;
import static com.zerobase.babdeusilbun.dto.TeamPurchaseDto.UpdateRequest;
import static org.springframework.http.HttpStatus.CREATED;

import com.zerobase.babdeusilbun.dto.IndividualPurchaseDto;
import com.zerobase.babdeusilbun.dto.PurchaseDto.PurchaseResponse;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.IndividualPurchaseService;
import com.zerobase.babdeusilbun.service.PurchaseService;
import com.zerobase.babdeusilbun.service.TeamPurchaseService;
import com.zerobase.babdeusilbun.swagger.annotation.cart.UserCartSwagger.CreateIndividualPurchaseSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.cart.UserCartSwagger.CreateTeamPurchaseSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.cart.UserCartSwagger.DeleteIndividualPurchaseSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.cart.UserCartSwagger.DeleteTeamPurchaseSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.cart.UserCartSwagger.GetIndividualPurchaseCartSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.cart.UserCartSwagger.GetTeamPurchaseCartSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.cart.UserCartSwagger.UpdateIndividualPurchaseSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.cart.UserCartSwagger.UpdateTeamPurchaseSwagger;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserCartController {
    private final PurchaseService purchaseService;
    private final TeamPurchaseService teamPurchaseService;
    private final IndividualPurchaseService individualPurchaseService;

    @GetMapping("/meetings/{meetingId}/team-order")
    @GetTeamPurchaseCartSwagger
    public ResponseEntity<PurchaseResponse> getTeamPurchaseCart(
        @PathVariable("meetingId") Long meetingId,
        @Parameter(description = "공동 장바구니에서 보일 페이지번호와 한 페이지당 보이는 항목 개수")
        Pageable pageable
    ) {
        return ResponseEntity.ok(
            purchaseService.getTeamPurchaseCart(meetingId, pageable));
    }

    @PostMapping("/meetings/{meetingId}/team-purchases")
    @CreateTeamPurchaseSwagger
    public ResponseEntity<Void> createTeamPurchase(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("meetingId") Long meetingId,
            @Validated @RequestBody CreateRequest request) {
        teamPurchaseService.createTeamPurchase(userDetails.getId(), meetingId, request);

        return ResponseEntity.status(CREATED).build();
    }

    @PatchMapping("/team-purchases/{purchaseId}")
    @UpdateTeamPurchaseSwagger
    public ResponseEntity<Void> updateTeamPurchase(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("purchaseId") Long purchaseId,
            @Validated @RequestBody UpdateRequest request) {

        teamPurchaseService.updateTeamPurchase(userDetails.getId(), purchaseId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/team-purchases/{purchaseId}")
    @DeleteTeamPurchaseSwagger
    public ResponseEntity<Void> deleteTeamPurchase(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("purchaseId") Long purchaseId) {
        teamPurchaseService.deleteTeamPurchase(userDetails.getId(), purchaseId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/meetings/{meetingId}/individual-order")
    @GetIndividualPurchaseCartSwagger
    public ResponseEntity<PurchaseResponse> getIndividualPurchaseCart(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable("meetingId") Long meetingId,
        @Parameter(description = "개인 장바구니에서 보일 페이지번호와 한 페이지당 보이는 항목 개수") Pageable pageable
    ) {
        return ResponseEntity.ok(
            purchaseService.getIndividualPurchaseCart(userDetails.getId(), meetingId, pageable));
    }

    @PostMapping("/meetings/{meetingId}/individual-purchases")
    @CreateIndividualPurchaseSwagger
    public ResponseEntity<Void> createIndividualPurchase(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable("meetingId") Long meetingId,
        @Validated @RequestBody IndividualPurchaseDto.CreateRequest request) {
        individualPurchaseService.createIndividualPurchase(userDetails.getId(), meetingId, request);

        return ResponseEntity.status(CREATED).build();
    }

    @PatchMapping("/individual-purchases/{purchaseId}")
    @UpdateIndividualPurchaseSwagger
    public ResponseEntity<Void> updateIndividualPurchase(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable("purchaseId") Long purchaseId,
        @Validated @RequestBody IndividualPurchaseDto.UpdateRequest request) {

        individualPurchaseService.updateIndividualPurchase(userDetails.getId(), purchaseId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/individual-purchases/{purchaseId}")
    @DeleteIndividualPurchaseSwagger
    public ResponseEntity<Void> deleteIndividualPurchase(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable("purchaseId") Long purchaseId) {
        individualPurchaseService.deleteIndividualPurchase(userDetails.getId(), purchaseId);

        return ResponseEntity.ok().build();
    }
}
