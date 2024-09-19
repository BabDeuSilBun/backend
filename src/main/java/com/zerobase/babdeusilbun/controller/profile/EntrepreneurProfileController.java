package com.zerobase.babdeusilbun.controller.profile;

import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.zerobase.babdeusilbun.dto.EntrepreneurDto.MyPage;
import com.zerobase.babdeusilbun.dto.EntrepreneurDto.UpdateRequest;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.EntrepreneurService;
import com.zerobase.babdeusilbun.swagger.annotation.profile.EntrepreneurProfileSwagger.GetProfileSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.profile.EntrepreneurProfileSwagger.UpdateProfileSwagger;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/businesses")
@PreAuthorize("hasRole('ENTREPRENEUR')")
@RequiredArgsConstructor
public class EntrepreneurProfileController {
  private final EntrepreneurService entrepreneurService;

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
  @PatchMapping(value ="/infos",
      consumes = {MULTIPART_FORM_DATA_VALUE, APPLICATION_JSON_VALUE})
  @UpdateProfileSwagger
  public ResponseEntity<Void> updateProfile(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @Parameter(description = "10MB 이하의 변경할 이미지(없다면 입력X)")
      @RequestPart(value = "file", required = false) MultipartFile image,
      @Parameter(description = "회원 정보 수정사항")
      @RequestPart("request") UpdateRequest request
  ) {

    UpdateRequest result = entrepreneurService.updateProfile(entrepreneur.getId(), image, request);

    return (image != null && result.getImage() == null) ?
        ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
  }
}
