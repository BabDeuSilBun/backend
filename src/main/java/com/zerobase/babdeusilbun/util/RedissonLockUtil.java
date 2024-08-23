package com.zerobase.babdeusilbun.util;

public class RedissonLockUtil {

  public static final String REDISSON_LOCK_PAYMENT_PREFIX = "payment:";

  public static String getPaymentLockKey(Long userId) {
    return REDISSON_LOCK_PAYMENT_PREFIX + userId.toString();
  }

  public static String getPaymentLockKey(String userId) {
    return REDISSON_LOCK_PAYMENT_PREFIX + userId;
  }

}
