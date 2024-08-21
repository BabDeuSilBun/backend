package com.zerobase.babdeusilbun.dto;

import org.springframework.beans.factory.annotation.Value;

public class StoreSchoolDto {
  public interface Information {
    @Value("#{target.school.id}")
    Long getSchoolId();
    @Value("#{T(com.zerobase.babdeusilbun.util.ConverterUtility).schoolNameConvert(target.school.name)}")
    String getName();
    @Value("#{target.school.campus}")
    String getCampus();
  }
}
