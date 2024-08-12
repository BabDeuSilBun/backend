package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.SignDto;

public interface EmailService {
  void sendVerificationCode(SignDto.VerifyEmailRequest request);

  SignDto.VerifyCodeResponse verifyCode(SignDto.VerifyCodeRequest request);
}
