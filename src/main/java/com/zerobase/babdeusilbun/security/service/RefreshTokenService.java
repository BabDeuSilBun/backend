package com.zerobase.babdeusilbun.security.service;

import com.zerobase.babdeusilbun.security.dto.RefreshToken;

public interface RefreshTokenService {

  String createRefreshToken(String jwtToken, String email);

  boolean isExpiredRefreshToken(RefreshToken refreshToken);

  RefreshToken findRefreshTokenByEmail(String email);

  boolean tokenIsMatch(String curJwtToken, String curRefreshToken);

}
