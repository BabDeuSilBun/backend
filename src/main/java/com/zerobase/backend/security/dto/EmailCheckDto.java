package com.zerobase.backend.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class EmailCheckDto {

  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Getter @Builder
  public static class Request {

    @Email
    @NotBlank(message = "email 항목은 빈값이 올 수 없습니다.")
    private String email;

  }

  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Getter
  public static class Response {
    private Boolean usable;

    public static Response of(boolean usable) {
      return new Response(usable);
    }
  }



}
