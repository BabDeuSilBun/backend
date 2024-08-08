package com.zerobase.backend.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class RefreshTokenRequest {

  @NotBlank(message = "refresh token은 빈 값이 될 수 없습니다.")
  private String refreshToken;
}
