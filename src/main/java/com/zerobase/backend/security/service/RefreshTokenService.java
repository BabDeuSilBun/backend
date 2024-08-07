package com.zerobase.backend.security.service;

import com.zerobase.backend.security.dto.RefreshToken;

public interface RefreshTokenService {

  String createRefreshToken(String jwtToken, String email);

  boolean isExpiredRefreshToken(RefreshToken refreshToken);

  RefreshToken findRefreshTokenByEmail(String email);

  boolean tokenIsMatch(String curJwtToken, String curRefreshToken);

}
