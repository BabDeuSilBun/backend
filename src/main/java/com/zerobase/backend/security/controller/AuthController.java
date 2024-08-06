package com.zerobase.backend.security.controller;

import com.zerobase.backend.security.dto.EmailCheckDto;
import com.zerobase.backend.security.dto.SignRequest;
import com.zerobase.backend.security.dto.SignResponse;
import com.zerobase.backend.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  /**
   * 이메일 중복확인
   */
  @PostMapping("/signup/email-duplicated")
  public ResponseEntity<?> checkEmail(@Validated @RequestBody EmailCheckDto.Request request) {
    boolean usable = authService.checkEmailIsDup(request.getEmail());

    return ResponseEntity.ok(EmailCheckDto.Response.of(usable));
  }


  /**
   * 사용자 회원가입
   */
  @PostMapping("/user/signup")
  public ResponseEntity<?> userSignup(SignRequest.UserSignUp request) {

    authService.userSignup(request);

    return ResponseEntity.ok(null);
  }

  /**
   * 사업자 회원가입
   */
  @PostMapping("/business/signup")
  public ResponseEntity<?> businessSignup(SignRequest.BusinessSignUp request) {

    authService.businessSignup(request);

    return ResponseEntity.ok(null);
  }

  /**
   * 로그인
   */
  @PostMapping("/signin")
  public ResponseEntity<?> signin(@Validated @RequestBody SignRequest.SignIn request) {

    SignResponse response = authService.signin(request);

    return ResponseEntity.ok(null);
  }

  /**
   * 로그아웃
   */
  @PostMapping("/logout")
  public ResponseEntity<?> logout() {

    return ResponseEntity.ok(null);
  }

}
