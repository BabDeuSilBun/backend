package com.zerobase.babdeusilbun.security.controller;

import static com.zerobase.babdeusilbun.security.constants.SecurityConstantsUtil.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.zerobase.babdeusilbun.dto.SignDto;
import com.zerobase.babdeusilbun.security.application.AuthApplication;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.security.dto.EmailCheckDto;
import com.zerobase.babdeusilbun.security.dto.SignRequest;
import com.zerobase.babdeusilbun.security.dto.SignResponse;
import com.zerobase.babdeusilbun.security.dto.WithdrawalRequest;
import com.zerobase.babdeusilbun.security.service.JwtValidationService;
import com.zerobase.babdeusilbun.security.service.SignService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
   * 비밀번호 확인
   */
  @PostMapping("/password-confirm")
  public ResponseEntity<SignDto.VerifyPasswordResponse> passwordConfirm(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestBody SignDto.VerifyPasswordRequest request) {

    return ResponseEntity.ok(signService.passwordConfirm(request, user.getId()));
  }

  /**
   * 사용자 이메일 중복확인
   */
  @PostMapping("/users/email-duplicated")
  public ResponseEntity<?> checkEmailForUser(@Validated @RequestBody EmailCheckDto.Request request) {

    return ResponseEntity.ok(
        EmailCheckDto.Response.of(signService.isUserEmailIsUnique(request.getEmail()))
    );
  }

  /**
   * 사업자 이메일 중복확인
   */
  @PostMapping("/businesses/email-duplicated")
  public ResponseEntity<?> checkEmailForBusiness(@Validated @RequestBody EmailCheckDto.Request request) {

    return ResponseEntity.ok(
            EmailCheckDto.Response.of(signService.isEntrepreneurEmailIsUnique(request.getEmail()))
    );
  }

  /**
   * 사용자 회원가입
   */
  @PostMapping("/users/signup")
  public ResponseEntity<?> userSignup(@Validated @RequestBody SignRequest.UserSignUp request) {

    signService.userSignUp(request);

    return ResponseEntity.status(CREATED).build();
  }

  /**
   * 사업자 회원가입
   */
  @PostMapping("/businesses/signup")
  public ResponseEntity<?> businessSignup(
      @Validated @RequestBody SignRequest.BusinessSignUp request) {

    signService.entrepreneurSignUp(request);

    return ResponseEntity.status(CREATED).build();
  }

  /**
   * 사용자 로그인
   */
  @PostMapping("/users/signin")
  public ResponseEntity<?> userSignin(
      @Validated @RequestBody SignRequest.SignIn request,
      HttpServletResponse servletResponse
  ) {

    return ResponseEntity.ok(authApplication.userSignin(request, servletResponse));
  }

  /**
   * 사업자 로그인
   */
  @PostMapping("/businesses/signin")
  public ResponseEntity<?> businessSignin(
      @Validated @RequestBody SignRequest.SignIn request,
      HttpServletResponse servletResponse
  ) {

    SignResponse response = authApplication.entrepreneurSignin(request, servletResponse);

    return ResponseEntity.ok(response);
  }

  /**
   * 로그아웃
   */
  @PreAuthorize("hasAnyRole('USER', 'ENTREPRENEUR')")
  @PostMapping("/logout")
  public ResponseEntity<?> logout(
      @RequestHeader(AUTHORIZATION_HEADER_NAME) String authorizationHeader,
      HttpServletResponse servletResponse
  ) {

    String jwtToken = jwtValidationService.verifyJwtFromHeader(authorizationHeader);

    authApplication.logout(jwtToken, servletResponse);

    return ResponseEntity.status(OK).build();
  }

  /**
   * 사용자 회원탈퇴
   */
  @PreAuthorize("hasAnyRole('USER')")
  @PostMapping("/users/withdrawal")
  public ResponseEntity<?> userWithdrawal(
      @RequestHeader(AUTHORIZATION_HEADER_NAME) String authorizationHeader,
      @Validated @RequestBody WithdrawalRequest request,
      HttpServletResponse servletResponse
  ) {

    String jwtToken = jwtValidationService.verifyJwtFromHeader(authorizationHeader);

    authApplication.userWithdrawal(jwtToken, request, servletResponse);

    return ResponseEntity.status(OK).build();
  }

  /**
   * 사업자 회원탈퇴
   */
  @PreAuthorize("hasAnyRole('ENTREPRENEUR')")
  @PostMapping("/businesses/withdrawal")
  public ResponseEntity<?> entrepreneurWithdrawal(
      @RequestHeader(AUTHORIZATION_HEADER_NAME) String authorizationHeader,
      @Validated @RequestBody WithdrawalRequest request,
      HttpServletResponse servletResponse
  ) {

    String jwtToken = jwtValidationService.verifyJwtFromHeader(authorizationHeader);

    authApplication.entrepreneurWithdrawal(jwtToken, request, servletResponse);

    return ResponseEntity.status(OK).build();
  }

  /**
   * 토큰 재발급
   */
  @PostMapping("/refresh-token")
  public ResponseEntity<?> refreshToken(
      HttpServletRequest servletRequest, HttpServletResponse servletResponse
  ) {

    SignResponse response = authApplication.reGenerateToken(servletRequest, servletResponse);

    return ResponseEntity.status(CREATED).body(response);
  }
}
