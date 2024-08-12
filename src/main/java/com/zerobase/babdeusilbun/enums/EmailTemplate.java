package com.zerobase.babdeusilbun.enums;

import lombok.Getter;

@Getter
public enum EmailTemplate {
  VERIFY_EMAIL("회원가입 시 이메일 유효성 검증 이메일 템플릿",
      "[밥드실분] 회원가입 이메일 인증 코드입니다.", "verify-email");

  private final String description;
  private final String subject;
  private final String html;

  EmailTemplate(String description, String subject, String html) {
    this.description = description;
    this.subject = subject;
    this.html = html;
  }
}
