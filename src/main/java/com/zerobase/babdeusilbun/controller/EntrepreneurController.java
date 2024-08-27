package com.zerobase.babdeusilbun.controller;

import com.zerobase.babdeusilbun.dto.EntrepreneurDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.EntrepreneurService;
import com.zerobase.babdeusilbun.swagger.annotation.EntrepreneurSwagger;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.zerobase.babdeusilbun.dto.EntrepreneurDto.*;
import static com.zerobase.babdeusilbun.swagger.annotation.EntrepreneurSwagger.*;
import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;
import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
public class EntrepreneurController {

    private final EntrepreneurService entrepreneurService;

    /**
     * 회원 정보 조회 (로그인한 사업가 정보 조회)
     */
    @PreAuthorize("hasRole('ENTREPRENEUR')")
    @GetMapping("/infos")
    @GetProfileSwagger
    public ResponseEntity<MyPage> getProfile(@AuthenticationPrincipal CustomUserDetails entrepreneur) {
        MyPage myPage = entrepreneurService.getMyPage(entrepreneur.getId());

        return ResponseEntity.ok(myPage);
    }

    /**
     * 회원 정보 수정 (로그인한 사업가 정보 수정)
     */
    @ApiResponse
    @PreAuthorize("hasRole('ENTREPRENEUR')")
    @PatchMapping(value ="/infos",
            consumes = {MULTIPART_FORM_DATA_VALUE, APPLICATION_JSON_VALUE})
    @UpdateProfileSwagger
    public ResponseEntity<Void> updateProfile(
            @AuthenticationPrincipal CustomUserDetails entrepreneur,
            @RequestPart(value = "file", required = false) MultipartFile image,
            @RequestPart UpdateRequest request
    ) {

        UpdateRequest result = entrepreneurService.updateProfile(entrepreneur.getId(), image, request);

        return (image != null && result.getImage() == null) ?
                ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
    }
}
