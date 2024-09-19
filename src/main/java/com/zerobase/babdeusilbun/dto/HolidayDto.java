package com.zerobase.babdeusilbun.dto;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

public class HolidayDto {
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode
  public static class HolidaysRequest {
    private Set<DayOfWeek> holidays = new HashSet<>();
  }

  public interface Information {
    @Value("#{target.id}")
    Long getHolidayId();
    @Value("#{T(com.zerobase.babdeusilbun.util.ConverterUtility).dayOfWeekConvert(target.dayOfWeek)}")
    String getDayOfWeek();
  }
}
