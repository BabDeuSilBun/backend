package com.zerobase.babdeusilbun.controller;

import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;

import com.zerobase.babdeusilbun.dto.UserDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;


  /**
   * 내 정보 조회
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/my-page")
  public ResponseEntity<?> getMyProfile() {
    UserDto.MyPage myPage= userService.getMyPage();
    return ResponseEntity.ok(myPage);
  }
  /**
   * 내 정보 수정
   */
  @PreAuthorize("hasRole('USER')")
  @PatchMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Void> updateProfile(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestPart(value = "file", required = false) MultipartFile image,
      @RequestPart(value = "request") UserDto.UpdateRequest request) {
    UserDto.UpdateRequest changeRequest = userService.updateProfile(user.getId(), image, request);

    return (image != null && changeRequest.getImage() == null) ||
        (request.getSchoolId() != null && changeRequest.getSchoolId() == null) ||
        (request.getMajorId() != null && changeRequest.getMajorId() == null) ?
        ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
  }
}
