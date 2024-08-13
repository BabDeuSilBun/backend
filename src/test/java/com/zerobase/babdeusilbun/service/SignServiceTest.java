package com.zerobase.babdeusilbun.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.zerobase.babdeusilbun.dto.SignDto.VerifyPasswordRequest;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyPasswordResponse;
import com.zerobase.babdeusilbun.security.service.impl.SignServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class SignServiceTest {
  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private SignServiceImpl signService;

  @DisplayName("비밀번호 확인 성공 테스트")
  @Test
  void passwordConfirmSuccess() {
    // given
    String rawPassword = "testPassword";
    String encodedPassword = "encodedPassword";
    VerifyPasswordRequest request = new VerifyPasswordRequest(rawPassword);

    // when
    when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

    VerifyPasswordResponse response = signService.passwordConfirm(request, encodedPassword);

    // then
    assertTrue(response.isCorrected());
  }

  @DisplayName("비밀번호 확인 실패 테스트")
  @Test
  void passwordConfirmFailed() {
    // given
    String rawPassword = "testPassword";
    String encodedPassword = "encodedPassword";
    VerifyPasswordRequest request = new VerifyPasswordRequest(rawPassword);

    // when
    when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

    VerifyPasswordResponse response = signService.passwordConfirm(request, encodedPassword);

    // then
    assertFalse(response.isCorrected());
  }
}
