package com.zerobase.babdeusilbun.security.service.impl;

import static com.zerobase.babdeusilbun.exception.ErrorCode.REFRESH_TOKEN_NOT_FOUND;
import static com.zerobase.babdeusilbun.security.redis.RedisKeyUtil.refreshTokenKey;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.zerobase.babdeusilbun.security.dto.RefreshToken;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.security.service.RefreshTokenService;
import com.zerobase.babdeusilbun.security.util.JwtComponent;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

  private final RedisTemplate<String, RefreshToken> redisTemplate;

  private final JwtComponent jwtComponent;

  @Value("${refresh-token.expire-ms}")
  private String refreshTokenExpiredMs;

  /**
   * refresh token 생성
   */
  @Override
  public String createRefreshToken(String jwtToken, String email) {

    // 새로운 refresh token 생성
    RefreshToken refreshToken = createNewRefreshToken(jwtToken, email);

    // redis에 저장
    redisTemplate.opsForValue().set
        (refreshTokenKey(email), refreshToken, Long.parseLong(refreshTokenExpiredMs), MILLISECONDS);

    return refreshToken.getRefreshToken();
  }

  /**
   * refresh token 만료 확인
   */
  @Override
  public boolean isExpiredRefreshToken(RefreshToken refreshToken) {
    return refreshToken.getExpiredDate().before(new Date());
  }


  /**
   * email로 redis에서 refresh token 객체 검색
   */
  @Override
  public RefreshToken findRefreshTokenByEmail(String email) {

    RefreshToken refreshToken = redisTemplate.opsForValue().get(refreshTokenKey(email));

    // refresh token이 redis에 존재하지 않을 경우 예외 발생
    verifyRefreshTokenIsExist(refreshToken);

    return refreshToken;
  }

  /**
   * jwt, refresh token이 유효한 짝인지 확인
   */
  @Override
  public boolean tokenIsMatch(String curJwtToken, String curRefreshToken) {

    String findEmail = jwtComponent.getEmail(curJwtToken);
    RefreshToken findRefreshToken = findRefreshTokenByEmail(findEmail);

    return curJwtToken.equals(findRefreshToken.getJwtToken())
        && curRefreshToken.equals(findRefreshToken.getRefreshToken());
  }

  /**
   * 새로운 refresh token 객체 생성
   */
  private RefreshToken createNewRefreshToken(String jwtToken, String email) {
    return RefreshToken.builder()
        .email(email)
        .refreshToken(UUID.randomUUID().toString())
        .jwtToken(jwtToken)
        .expiredDate(new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpiredMs)))
        .build();
  }

  private void verifyRefreshTokenIsExist(RefreshToken refreshToken) {
    if(refreshToken == null) {
      throw new CustomException(REFRESH_TOKEN_NOT_FOUND);
    }
  }
}
