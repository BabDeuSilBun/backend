package com.zerobase.babdeusilbun.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SignDto {
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class VerifyEmailRequest {
    private String email;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class VerifyCodeRequest {
    private String email;
    private String code;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class VerifyCodeResponse {
    private boolean result;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class VerifyPasswordRequest {
    private String password;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class VerifyPasswordResponse {
    @JsonProperty("isCorrected")
    private boolean isCorrected;
  }
}
