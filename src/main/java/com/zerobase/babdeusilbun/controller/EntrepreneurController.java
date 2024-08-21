package com.zerobase.babdeusilbun.controller;

import com.zerobase.babdeusilbun.dto.EntrepreneurDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.EntrepreneurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;

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
    public ResponseEntity<EntrepreneurDto.MyPage> getProfile(@AuthenticationPrincipal CustomUserDetails entrepreneur) {
        EntrepreneurDto.MyPage myPage = entrepreneurService.getMyPage(entrepreneur.getId());

        return ResponseEntity.ok(myPage);
    }

    /**
     * 회원 정보 수정 (로그인한 사업가 정보 수정)
     */
    @PreAuthorize("hasRole('ENTREPRENEUR')")
    @PatchMapping(value ="/infos",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> updateProfile(
            @AuthenticationPrincipal CustomUserDetails entrepreneur,
            @RequestPart(value = "file", required = false) MultipartFile image,
            @RequestPart(value = "request") EntrepreneurDto.UpdateRequest request
    ) {

        EntrepreneurDto.UpdateRequest result = entrepreneurService.updateProfile(entrepreneur.getId(), image, request);

        return (image != null && result.getImage() == null) ?
                ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
    }
}
