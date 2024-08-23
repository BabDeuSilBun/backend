package com.zerobase.babdeusilbun.dto;

import org.springframework.beans.factory.annotation.Value;

public class StoreCategoryDto {
  public interface Information {
    @Value("#{target.category.id}")
    Long getCategoryId();
    @Value("#{target.category.name}")
    String getName();
  }
}
