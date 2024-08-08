package com.zerobase.babdeusilbun.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WithdrawalRequest {

  @NotBlank(message = "password는 빈 값이 올 수 없습니다.")
  private String password;

}
