package com.zerobase.backend.security.service.impl;

import static com.zerobase.backend.security.exception.SecurityErrorCode.*;
import static com.zerobase.backend.security.redis.RedisKeyUtil.*;

import com.zerobase.backend.security.dto.RefreshToken;
import com.zerobase.backend.security.exception.SecurityCustomException;
import com.zerobase.backend.security.service.RefreshTokenService;
import com.zerobase.backend.security.util.JwtComponent;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
  private String expiredMs;

  /**
   * refresh token 생성
   */
  @Override
  public String createRefreshToken(String jwtToken, String email) {

    // 새로운 refresh token 생성
    RefreshToken refreshToken = createNewRefreshToken(jwtToken, email);

    // redis에 저장하기 위해 HashMap으로 생성
    HashMap<String, Object> refreshTokenMap = new HashMap<>();
    refreshTokenMap.put("email", refreshToken.getEmail());
    refreshTokenMap.put("refreshToken", refreshToken.getRefreshToken());
    refreshTokenMap.put("jwtToken", refreshToken.getJwtToken());
    refreshTokenMap.put("expiredDate", refreshToken.getExpiredDate());

    // redis에 저장
    redisTemplate.opsForHash().put(REFRESH_TOKEN, refreshToken.getEmail(), refreshTokenMap);

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

    // redis에서 refresh token을 HashMap으로 가져옴
    Map<Object, Object> refreshTokenMap =
        (Map<Object, Object>) redisTemplate.opsForHash()
            .entries(REFRESH_TOKEN)
            .get(email);

    // refresh token이 redis에 존재하지 않을 경우 예외 발생
    verifyRefreshToken(refreshTokenMap);

    // HashMap 형태의 refresh token을 객체로 변경
    return fromMap(refreshTokenMap);
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
        .expiredDate(new Date(System.currentTimeMillis() + Long.parseLong(expiredMs)))
        .build();
  }

  private void verifyRefreshToken(Map<Object, Object> refreshTokenMap) {
    if (refreshTokenMap == null) {
      throw new SecurityCustomException(REFRESH_TOKEN_INVALID);
    }
  }

  private RefreshToken fromMap(Map<Object, Object> map) {
    return RefreshToken.builder()
        .email((String) map.get("email"))
        .refreshToken((String) map.get("refreshToken"))
        .jwtToken((String) map.get("jwtToken"))
        .expiredDate((Date) map.get("expiredDate"))
        .build();
  }
}
