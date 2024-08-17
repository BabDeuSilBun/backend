package com.zerobase.babdeusilbun.controller;

import com.zerobase.babdeusilbun.dto.EvaluateDto;
import com.zerobase.babdeusilbun.service.EvaluateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<?> getMyEvaluates() {
        EvaluateDto.MyEvaluates myEvaluates = evaluateService.getEvaluates();
        return ResponseEntity.ok(myEvaluates);
    }
}
