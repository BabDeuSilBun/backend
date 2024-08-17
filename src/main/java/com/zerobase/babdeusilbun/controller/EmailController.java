package com.zerobase.babdeusilbun.controller;

import com.zerobase.babdeusilbun.dto.SignDto;
import com.zerobase.babdeusilbun.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmailController {
  private final EmailService emailService;

  /**
   * 이메일 유효성 검증 이메일 전송
   */
  @PostMapping("/signup/email-verify")
  public ResponseEntity<Void> sendEmailVerifyCode(@RequestBody SignDto.VerifyEmailRequest request) {
    emailService.sendVerificationCode(request);

    return ResponseEntity.ok().build();
  }


  /**
   * 이메일 유효성 검증 코드 검증
   */
  @PostMapping("/signup/email-verify/confirm")
  public ResponseEntity<SignDto.VerifyCodeResponse> confirmEmailVerifyCode(
      @RequestBody SignDto.VerifyCodeRequest request) {
    return ResponseEntity.ok(emailService.verifyCode(request));
  }
}
