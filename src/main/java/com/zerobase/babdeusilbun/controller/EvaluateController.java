package com.zerobase.babdeusilbun.controller;

import com.zerobase.babdeusilbun.dto.EvaluateDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.EvaluateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class EvaluateController {
    private final EvaluateService evaluateService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/evaluates")
    public ResponseEntity<EvaluateDto.MyEvaluates> getMyEvaluates(@AuthenticationPrincipal CustomUserDetails user) {
        EvaluateDto.MyEvaluates myEvaluates = evaluateService.getEvaluates(user.getId());
        return ResponseEntity.ok(myEvaluates);
    }
}
