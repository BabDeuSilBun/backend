package com.zerobase.backend.security.application;

import static com.zerobase.backend.security.exception.SecurityErrorCode.EMAIL_NOT_FOUND;

import com.zerobase.backend.repository.EntrepreneurRepository;
import com.zerobase.backend.repository.UserRepository;
import com.zerobase.backend.security.dto.SignRequest.SignIn;
import com.zerobase.backend.security.dto.SignResponse;
import com.zerobase.backend.security.exception.SecurityCustomException;
import com.zerobase.backend.security.service.RefreshTokenService;
import com.zerobase.backend.security.service.SignService;
import com.zerobase.backend.security.service.CustomUserDetailsService;
import com.zerobase.backend.security.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthApplication {

  private final SignService signService;
  private final RefreshTokenService refreshTokenService;

  private final UserRepository userRepository;
  private final EntrepreneurRepository entrepreneurRepository;


  public SignResponse signin(SignIn request) {

    String email = request.getEmail();
    String jwtToken = null;

    boolean isUser = userRepository.existsByEmail(email);
    boolean isEntrepreneur = entrepreneurRepository.existsByEmail(email);

    verifyExistAuthentication(isUser, isEntrepreneur);

    if(isUser) {
      jwtToken = signService.UserSignin(request);
    }
    if (isEntrepreneur) {
      jwtToken = signService.EntrepreneurSignIn(request);
    }

    String refreshToken = refreshTokenService.createRefreshToken(email);

    return SignResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
  }

  private void verifyExistAuthentication(boolean isUser, boolean isEntrepreneur) {
    if (!isUser && !isEntrepreneur) {
      throw new SecurityCustomException(EMAIL_NOT_FOUND);
    }
  }
}
