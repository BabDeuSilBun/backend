package com.zerobase.babdeusilbun.util;

public class RedissonLockUtil {

  public static final String REDISSON_LOCK_POINT_PREFIX = "point:";

  public static String getPointLockKey(Long userId) {
    return REDISSON_LOCK_POINT_PREFIX + userId.toString();
  }

}
