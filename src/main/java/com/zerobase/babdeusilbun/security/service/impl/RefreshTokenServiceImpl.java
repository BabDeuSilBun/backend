package com.zerobase.babdeusilbun.security.service.impl;

import static com.zerobase.babdeusilbun.exception.ErrorCode.REFRESH_TOKEN_NOT_FOUND;
import static com.zerobase.babdeusilbun.security.redis.RedisKeyUtil.refreshTokenKey;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.zerobase.babdeusilbun.security.dto.RefreshToken;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.security.service.RefreshTokenService;
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

  @Value("${refresh-token.expire-second}")
  private Long refreshTokenExpiredSecond;

  /**
   * refresh token 생성
   */
  @Override
  public String createRefreshToken(String prefixedEmail) {

    // 새로운 refresh token 생성
    RefreshToken refreshToken = createNewRefreshToken(prefixedEmail);

    // redis에 저장
    redisTemplate.opsForValue().set
        (refreshTokenKey(refreshToken.getRefreshToken()), refreshToken, refreshTokenExpiredSecond, SECONDS);

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
   * redisToken으로 redis에서 refresh token 객체 검색
   */
  @Override
  public RefreshToken findDtoByRefreshToken(String refreshToken) {

    RefreshToken refreshTokenDto = redisTemplate.opsForValue().get(refreshTokenKey(refreshToken));

    // refresh token이 redis에 존재하지 않을 경우 예외 발생
    verifyRefreshTokenIsExist(refreshTokenDto);

    return refreshTokenDto;
  }

  /**
   * redis에서 refresh token 제거
   */
  @Override
  public void deleteRefreshTokenFromRedis(String refreshToken) {
    redisTemplate.delete(refreshTokenKey(refreshToken));
  }

  /**
   * 새로운 refresh token 객체 생성
   */
  private RefreshToken createNewRefreshToken(String prefixedEmail) {
    return RefreshToken.builder()
        .email(prefixedEmail)
        .refreshToken(UUID.randomUUID().toString())
        .expiredDate(new Date(System.currentTimeMillis() + (refreshTokenExpiredSecond * 1000))) // 1일
        .build();
  }

  private void verifyRefreshTokenIsExist(RefreshToken refreshToken) {
    if(refreshToken == null) {
      throw new CustomException(REFRESH_TOKEN_NOT_FOUND);
    }
  }
}
