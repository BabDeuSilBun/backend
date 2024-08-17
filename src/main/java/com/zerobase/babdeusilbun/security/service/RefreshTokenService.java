package com.zerobase.babdeusilbun.security.service;

import com.zerobase.babdeusilbun.security.dto.RefreshToken;

public interface RefreshTokenService {

  String createRefreshToken(String prefixedEmail);

  boolean isExpiredRefreshToken(RefreshToken refreshToken);

  RefreshToken findDtoByRefreshToken(String refreshToken);

  void deleteRefreshTokenFromRedis(String refreshToken);

//  boolean tokenIsMatch(String curJwtToken, String curRefreshToken);

}
