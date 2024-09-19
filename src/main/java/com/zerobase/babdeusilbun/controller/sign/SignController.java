package com.zerobase.babdeusilbun.controller.sign;

import com.zerobase.babdeusilbun.dto.SignDto;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyCodeRequest;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyCodeResponse;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyEmailRequest;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyPasswordResponse;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.security.service.SignService;
import com.zerobase.babdeusilbun.service.EmailService;
import com.zerobase.babdeusilbun.swagger.annotation.sign.SignSwagger.ConfirmEmailVerifyCodeSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.sign.SignSwagger.PasswordConfirmSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.sign.SignSwagger.SendEmailVerifyCodeSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SignController {
  private final SignService signService;
  private final EmailService emailService;

  /**
   * 비밀번호 확인
   */
  @PreAuthorize("isAuthenticated()")
  @PostMapping("/password-confirm")
  @PasswordConfirmSwagger
  public ResponseEntity<VerifyPasswordResponse> passwordConfirm(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestBody SignDto.VerifyPasswordRequest request) {

    return ResponseEntity.ok(signService.passwordConfirm(request, user.getRole(), user.getId()));
  }

  /**
   * 이메일 유효성 검증 이메일 전송
   */
  @PostMapping("/signup/email-verify")
  @SendEmailVerifyCodeSwagger
  public ResponseEntity<Void> sendEmailVerifyCode(@RequestBody VerifyEmailRequest request) {
    emailService.sendVerificationCode(request);

    return ResponseEntity.ok().build();
  }

  /**
   * 이메일 유효성 검증 코드 검증
   */
  @PostMapping("/signup/email-verify/confirm")
  @ConfirmEmailVerifyCodeSwagger
  public ResponseEntity<VerifyCodeResponse> confirmEmailVerifyCode(
      @RequestBody VerifyCodeRequest request) {
    return ResponseEntity.ok(emailService.verifyCode(request));
  }
}
