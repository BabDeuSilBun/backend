package com.zerobase.babdeusilbun.controller.profile;

import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.zerobase.babdeusilbun.dto.EvaluateDto.MyEvaluates;
import com.zerobase.babdeusilbun.dto.UserDto.MyPage;
import com.zerobase.babdeusilbun.dto.UserDto.UpdateAccount;
import com.zerobase.babdeusilbun.dto.UserDto.UpdateAddress;
import com.zerobase.babdeusilbun.dto.UserDto.UpdateRequest;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.EvaluateService;
import com.zerobase.babdeusilbun.service.UserService;
import com.zerobase.babdeusilbun.swagger.annotation.profile.UserProfileSwagger.GetMyEvaluatesSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.profile.UserProfileSwagger.GetMyProfileSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.profile.UserProfileSwagger.UpdateAccountSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.profile.UserProfileSwagger.UpdateAddressSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.profile.UserProfileSwagger.UpdateProfileSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserProfileController {
  private final UserService userService;
  private final EvaluateService evaluateService;

  /**
   * 내 정보 조회
   */
  @GetMapping("/my-page")
  @GetMyProfileSwagger
  public ResponseEntity<MyPage> getMyProfile(@AuthenticationPrincipal CustomUserDetails user) {
    MyPage myPage= userService.getMyPage(user.getId());
    return ResponseEntity.ok(myPage);
  }

  /**
   * 내 정보 수정
   */
  @PatchMapping(consumes = {MULTIPART_FORM_DATA_VALUE, APPLICATION_JSON_VALUE})
  @UpdateProfileSwagger
  public ResponseEntity<Void> updateProfile(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestPart(value = "file", required = false) MultipartFile image,
      @RequestPart(value = "request") UpdateRequest request) {
    UpdateRequest changeRequest = userService.updateProfile(user.getId(), image, request);

    return (image != null && changeRequest.getImage() == null) ||
        (request.getSchoolId() != null && changeRequest.getSchoolId() == null) ||
        (request.getMajorId() != null && changeRequest.getMajorId() == null) ?
        ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
  }

  /**
   * 내 주소 수정
   */
  @PutMapping("/address")
  @UpdateAddressSwagger
  public ResponseEntity<Void> updateAddress(
      @AuthenticationPrincipal CustomUserDetails user,
      @Validated @RequestBody UpdateAddress updateAddress) {
    userService.updateAddress(user.getId(), updateAddress);
    return ResponseEntity.ok().build();
  }

  /**
   * 내 계좌 수정
   */
  @PutMapping("/account")
  @UpdateAccountSwagger
  public ResponseEntity<Void> updateAccount(
      @AuthenticationPrincipal CustomUserDetails user,
      @Validated @RequestBody UpdateAccount updateAccount) {
    userService.updateAccount(user.getId(), updateAccount);
    return ResponseEntity.ok().build();
  }

  /**
   * 받은 평가 내역 조회
   */
  @GetMapping("/evaluates")
  @GetMyEvaluatesSwagger
  public ResponseEntity<MyEvaluates> getMyEvaluates(
      @AuthenticationPrincipal CustomUserDetails user
  ) {
    return ResponseEntity.ok(evaluateService.getEvaluates(user.getId()));
  }
}
