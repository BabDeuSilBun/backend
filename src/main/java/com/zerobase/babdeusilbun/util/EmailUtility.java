package com.zerobase.babdeusilbun.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class EmailUtility {
  public final static String EMAIL_CODE_PREFIX = "email:code:";
  public final static String EMAIL_COUNT_PREFIX = "email:count:";
  public final static int EMAIL_CODE_EXPIRATION_MINUTES = 60;
  public final static int EMAIL_VERIFY_MAX_COUNT = 5;

  public static Duration untilNextDay() {
    return Duration.between(LocalDateTime.now(), LocalDateTime.now().with(LocalTime.MAX));
  }
}
