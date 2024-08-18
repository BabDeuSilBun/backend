package com.zerobase.babdeusilbun.dto;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

public class HolidayDto {
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode
  public static class HolidaysRequest {
    private Set<DayOfWeek> holidays = new HashSet<>();
  }
}
