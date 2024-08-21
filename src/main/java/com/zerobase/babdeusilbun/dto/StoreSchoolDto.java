package com.zerobase.babdeusilbun.dto;

import org.springframework.beans.factory.annotation.Value;

public class StoreSchoolDto {
  public interface Information {
    @Value("#{target.school.id}")
    Long getSchoolId();
    @Value("#{target.school.name.replaceAll('(\\s|\\()[^\\s]+((캠퍼스)|\\))$', '', '')}")
    String getName();
    @Value("#{target.school.campus}")
    String getCampus();
  }
}
