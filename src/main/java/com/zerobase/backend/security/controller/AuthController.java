package com.zerobase.backend.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {


  /**
   * 회원가입
   */
  @PostMapping("/signup")
  public ResponseEntity<?> signup() {

    return ResponseEntity.ok(null);
  }

  /**
   * 로그인
   */
  @PostMapping("/signin")
  public ResponseEntity<?> signin() {

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
