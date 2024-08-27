package com.zerobase.babdeusilbun.controller;

import static com.zerobase.babdeusilbun.dto.EvaluateDto.*;
import static com.zerobase.babdeusilbun.swagger.annotation.EvaluateSwagger.*;
import static org.springframework.http.HttpStatus.*;

import com.zerobase.babdeusilbun.dto.EvaluateDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.EvaluateService;
import com.zerobase.babdeusilbun.swagger.annotation.EvaluateSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class EvaluateController {
    private final EvaluateService evaluateService;

    /**
     * 받은 평가 내역 조회
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/evaluates")
    @GetMyEvaluatesSwagger
    public ResponseEntity<MyEvaluates> getMyEvaluates(
        @AuthenticationPrincipal CustomUserDetails user
    ) {
      return ResponseEntity.ok(evaluateService.getEvaluates(user.getId()));
    }

    /**
     * 모임원 평가
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/meetings/{meetingId}/participants/{participantId}")
    @EvaluateParticipantSwagger
    public ResponseEntity<Void> evaluateParticipant(
        @RequestBody EvaluateParticipantRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long meetingId, @PathVariable Long participantId
    ) {

        evaluateService.evaluateParticipant(request, userDetails.getId(), meetingId, participantId);

        return ResponseEntity.status(CREATED).build();
    }

}
