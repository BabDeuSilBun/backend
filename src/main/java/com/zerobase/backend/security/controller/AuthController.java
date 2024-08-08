package com.zerobase.backend.security.controller;

import static org.springframework.http.HttpStatus.*;

import com.zerobase.backend.security.application.AuthApplication;
import com.zerobase.backend.security.dto.EmailCheckDto;
import com.zerobase.backend.security.dto.RefreshTokenRequest;
import com.zerobase.backend.security.dto.SignRequest;
import com.zerobase.backend.security.dto.SignResponse;
import com.zerobase.backend.security.service.JwtValidationService;
import com.zerobase.backend.security.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

  private final SignService signService;
  private final JwtValidationService jwtValidationService;
  private final AuthApplication authApplication;

  /**
   * 이메일 중복확인
   */
  @PostMapping("/signup/email-duplicated")
  public ResponseEntity<?> checkEmail(@Validated @RequestBody EmailCheckDto.Request request) {
    boolean usable = signService.isEmailIsUnique(request.getEmail());

    return ResponseEntity.ok(EmailCheckDto.Response.of(usable));
  }


  /**
   * 사용자 회원가입
   */
  @PostMapping("/user/signup")
  public ResponseEntity<?> userSignup(@Validated @RequestBody SignRequest.UserSignUp request) {

//    authApplication.signin(request)
    signService.userSignUp(request);

    return ResponseEntity.status(CREATED).body(null);
  }

  /**
   * 사업자 회원가입
   */
  @PostMapping("/business/signup")
  public ResponseEntity<?> businessSignup(@Validated @RequestBody SignRequest.BusinessSignUp request) {

    signService.entrepreneurSignUp(request);

    return ResponseEntity.status(CREATED).body(null);
  }

  /**
   * 로그인
   */
  @PostMapping("/signin")
  public ResponseEntity<?> signin(@Validated @RequestBody SignRequest.SignIn request) {

    SignResponse response = authApplication.signin(request);

    return ResponseEntity.ok(response);
  }

  /**
   * 로그아웃
   */
  @PostMapping("/logout")
  public ResponseEntity<?> logout(
      @RequestHeader("Authorization") String authorizationHeader
  ) {

    String jwtToken = jwtValidationService.verifyJwtFromHeader(authorizationHeader);

    signService.logout(jwtToken);

    return ResponseEntity.ok(null);
  }

  /**
   * 토큰 재발급
   */
  @PostMapping("/refresh-token")
  public ResponseEntity<?> refreshToken(
      @RequestHeader("Authorization") String authorizationHeader,
      @Validated @RequestBody RefreshTokenRequest request
  ) {

    String jwtToken = jwtValidationService.verifyJwtFromHeader(authorizationHeader);

    SignResponse response = authApplication.reGenerateToken(jwtToken, request.getRefreshToken());

    return ResponseEntity.status(CREATED).body(response);
  }

}
