package com.zerobase.backend.security.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.zerobase.backend.security.application.AuthApplication;
import com.zerobase.backend.security.dto.EmailCheckDto;
import com.zerobase.backend.security.dto.RefreshTokenRequest;
import com.zerobase.backend.security.dto.SignRequest;
import com.zerobase.backend.security.dto.SignResponse;
import com.zerobase.backend.security.dto.WithdrawalRequest;
import com.zerobase.backend.security.service.JwtValidationService;
import com.zerobase.backend.security.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    return ResponseEntity.ok(
        EmailCheckDto.Response.of(signService.isEmailIsUnique(request.getEmail()))
    );
  }


  /**
   * 사용자 회원가입
   */
  @PostMapping("/users/signup")
  public ResponseEntity<?> userSignup(@Validated @RequestBody SignRequest.UserSignUp request) {

//    authApplication.signin(request)
    signService.userSignUp(request);

    return ResponseEntity.status(CREATED).build();
  }

  /**
   * 사업자 회원가입
   */
  @PostMapping("/businesses/signup")
  public ResponseEntity<?> businessSignup(@Validated @RequestBody SignRequest.BusinessSignUp request) {

    signService.entrepreneurSignUp(request);

    return ResponseEntity.status(CREATED).build();
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

    return ResponseEntity.status(OK).build();
  }

  @PostMapping("/users/withdrawal")
  public ResponseEntity<?> userWithdrawal(
      @RequestHeader("Authorization") String authorizationHeader,
      @Validated @RequestBody WithdrawalRequest request
  ) {

    String jwtToken = jwtValidationService.verifyJwtFromHeader(authorizationHeader);

    signService.userWithdrawal(jwtToken, request);

    return ResponseEntity.status(OK).build();
  }

  @PostMapping("/businesses/withdrawal")
  public ResponseEntity<?> entrepreneurWithdrawal(
      @RequestHeader("Authorization") String authorizationHeader,
      @Validated @RequestBody WithdrawalRequest request
  ) {

    String jwtToken = jwtValidationService.verifyJwtFromHeader(authorizationHeader);

    signService.entrepreneurWithdrawal(jwtToken, request);

    return ResponseEntity.status(OK).build();
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
