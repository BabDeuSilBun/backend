package com.zerobase.babdeusilbun.controller;

import static com.zerobase.babdeusilbun.dto.SignDto.*;
import static com.zerobase.babdeusilbun.swagger.annotation.EmailSwagger.*;

import com.zerobase.babdeusilbun.dto.SignDto;
import com.zerobase.babdeusilbun.service.EmailService;
import com.zerobase.babdeusilbun.swagger.annotation.EmailSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/signup/email-verify")
@RequiredArgsConstructor
public class EmailController {
  private final EmailService emailService;

  /**
   * 이메일 유효성 검증 이메일 전송
   */
  @PostMapping
  @SendEmailVerifyCodeSwagger
  public ResponseEntity<Void> sendEmailVerifyCode(@RequestBody VerifyEmailRequest request) {
    emailService.sendVerificationCode(request);

    return ResponseEntity.ok().build();
  }


  /**
   * 이메일 유효성 검증 코드 검증
   */
  @PostMapping("/confirm")
  @ConfirmEmailVerifyCodeSwagger
  public ResponseEntity<VerifyCodeResponse> confirmEmailVerifyCode(
      @RequestBody VerifyCodeRequest request) {
    return ResponseEntity.ok(emailService.verifyCode(request));
  }
}
