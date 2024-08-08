package com.zerobase.babdeusilbun.security.redis;

public class RedisKeyUtil {

  // refresh token 저장
  public static final String REFRESH_TOKEN = "refreshToken";

  // 로그아웃용 jwt 블랙리스트 저장
  public static final String JWT_BLACKLIST = "jwtBlackList";


  public static String refreshTokenKey(String email) {
    return REFRESH_TOKEN + ":" + email;
  }

  public static String jwtBlackListKey(String jwt) {
    return JWT_BLACKLIST + ":" + jwt;
  }
}
