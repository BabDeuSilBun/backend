package com.zerobase.babdeusilbun.util;

import java.time.DayOfWeek;

public class ConverterUtility {
  public static String dayOfWeekConvert(DayOfWeek dayOfWeek) {
    if (dayOfWeek == null) {
      throw new IllegalArgumentException("DayOfWeek cannot be null");
    }

    return switch (dayOfWeek) {
      case MONDAY -> "월요일";
      case TUESDAY -> "화요일";
      case WEDNESDAY -> "수요일";
      case THURSDAY -> "목요일";
      case FRIDAY -> "금요일";
      case SATURDAY -> "토요일";
      case SUNDAY -> "일요일";
    };
  }

  public static String schoolNameConvert(String name) {
    if (name == null) {
      return null;
    }

    return name.replaceAll("(\\s|\\()[^\\s]+((캠퍼스)|\\))$", "");
  }
}
