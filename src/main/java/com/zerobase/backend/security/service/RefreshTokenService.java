package com.zerobase.backend.security.service;

import com.zerobase.backend.security.dto.RefreshToken;
import com.zerobase.backend.security.redis.RefreshTokenRepository;
import com.zerobase.backend.security.type.Role;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  @Value("${refresh-token.expire-ms}")
  private String expiredMs;

  public String createRefreshToken(String email) {

    RefreshToken refreshToken = createNewRefreshToken(email);
    RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);
    return savedRefreshToken.getToken();
  }

  private RefreshToken createNewRefreshToken(String email) {
    return RefreshToken.builder()
        .token(UUID.randomUUID().toString())
        .expiryDate(new Date(System.currentTimeMillis() + Long.parseLong(expiredMs)))
        .email(email)
        .build();
  }

}
