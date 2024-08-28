package com.zerobase.babdeusilbun.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyPasswordRequest;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyPasswordResponse;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.security.service.impl.SignServiceImpl;
import com.zerobase.babdeusilbun.util.TestUserUtility;
import java.util.Optional;
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

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private SignServiceImpl signService;

  @DisplayName("비밀번호 확인 성공 테스트")
  @Test
  void passwordConfirmSuccess() {
    // given
    String rawPassword = "testPassword";
    VerifyPasswordRequest request = new VerifyPasswordRequest(rawPassword);
    CustomUserDetails userDetails = TestUserUtility.createTestUser();
    User user = TestUserUtility.getUser();

    // when
    when(userRepository.findById(userDetails.getId())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(rawPassword, user.getPassword())).thenReturn(true);

    VerifyPasswordResponse response = signService.passwordConfirm(request, userDetails.getRole(), userDetails.getId());

    // then
    assertTrue(response.isCorrected());
  }

  @DisplayName("비밀번호 확인 실패 테스트")
  @Test
  void passwordConfirmFailed() {
    // given
    String rawPassword = "testPassword";
    VerifyPasswordRequest request = new VerifyPasswordRequest(rawPassword);
    CustomUserDetails userDetails = TestUserUtility.createTestUser();
    User user = TestUserUtility.getUser();

    // when
    when(userRepository.findById(userDetails.getId())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(rawPassword, user.getPassword())).thenReturn(false);

    VerifyPasswordResponse response = signService.passwordConfirm(request, userDetails.getRole(), userDetails.getId());

    // then
    assertFalse(response.isCorrected());
  }
}
