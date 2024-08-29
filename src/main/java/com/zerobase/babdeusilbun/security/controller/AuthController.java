package com.zerobase.babdeusilbun.security.controller;

import static com.zerobase.babdeusilbun.security.util.SecurityConstantsUtil.AUTHORIZATION_HEADER_NAME;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.zerobase.babdeusilbun.security.application.AuthApplication;
import com.zerobase.babdeusilbun.security.dto.SignRequest;
import com.zerobase.babdeusilbun.security.dto.SignResponse;
import com.zerobase.babdeusilbun.security.dto.WithdrawalRequest;
import com.zerobase.babdeusilbun.security.service.JwtValidationService;
import com.zerobase.babdeusilbun.swagger.annotation.auth.AuthSwagger.BusinessSigninSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.auth.AuthSwagger.EntrepreneurWithdrawalSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.auth.AuthSwagger.LogoutSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.auth.AuthSwagger.RefreshTokenSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.auth.AuthSwagger.UserSigninSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.auth.AuthSwagger.UserWithdrawalSwagger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
  private final JwtValidationService jwtValidationService;
  private final AuthApplication authApplication;

  @PostMapping("/users/signin")
  @UserSigninSwagger
  public ResponseEntity<SignResponse> userSignin(
      @Validated @RequestBody SignRequest.SignIn request,
      HttpServletResponse servletResponse
  ) {

    return ResponseEntity.ok(authApplication.userSignin(request, servletResponse));
  }

  @PostMapping("/businesses/signin")
  @BusinessSigninSwagger
  public ResponseEntity<SignResponse> businessSignin(
      @Validated @RequestBody SignRequest.SignIn request,
      HttpServletResponse servletResponse
  ) {

    return ResponseEntity.ok(authApplication.entrepreneurSignin(request, servletResponse));
  }

  @PostMapping("/logout")
  @LogoutSwagger
  public ResponseEntity<Void> logout(
      @RequestHeader(AUTHORIZATION_HEADER_NAME) String authorizationHeader,
      HttpServletResponse servletResponse
  ) {

    String jwtToken = jwtValidationService.verifyJwtFromHeader(authorizationHeader);

    authApplication.logout(jwtToken, servletResponse);

    return ResponseEntity.status(OK).build();
  }

  @PreAuthorize("hasAnyRole('USER')")
  @PostMapping("/users/withdrawal")
  @UserWithdrawalSwagger
  public ResponseEntity<Void> userWithdrawal(
      @RequestHeader(AUTHORIZATION_HEADER_NAME) String authorizationHeader,
      @Validated @RequestBody WithdrawalRequest request,
      HttpServletResponse servletResponse
  ) {

    String jwtToken = jwtValidationService.verifyJwtFromHeader(authorizationHeader);

    authApplication.userWithdrawal(jwtToken, request, servletResponse);

    return ResponseEntity.status(OK).build();
  }

  @PreAuthorize("hasAnyRole('ENTREPRENEUR')")
  @PostMapping("/businesses/withdrawal")
  @EntrepreneurWithdrawalSwagger
  public ResponseEntity<Void> entrepreneurWithdrawal(
      @RequestHeader(AUTHORIZATION_HEADER_NAME) String authorizationHeader,
      @Validated @RequestBody WithdrawalRequest request,
      HttpServletResponse servletResponse
  ) {

    String jwtToken = jwtValidationService.verifyJwtFromHeader(authorizationHeader);

    authApplication.entrepreneurWithdrawal(jwtToken, request, servletResponse);

    return ResponseEntity.status(OK).build();
  }

  @PostMapping("/refresh-token")
  @RefreshTokenSwagger
  public ResponseEntity<SignResponse> refreshToken(
      HttpServletRequest servletRequest, HttpServletResponse servletResponse
  ) {

    return ResponseEntity.status(CREATED).body(
        authApplication.reGenerateToken(servletRequest, servletResponse)
    );
  }
}
