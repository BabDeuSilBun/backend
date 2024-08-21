package com.zerobase.babdeusilbun.dto;

import com.zerobase.babdeusilbun.domain.Entrepreneur;
import lombok.Builder;
import lombok.Data;

public class EntrepreneurDto {
  @Data
  @Builder
  public static class SimpleInformation {
    private String name;
    private String businessNumber;
    private String image;

    public static SimpleInformation fromEntity(Entrepreneur entrepreneur) {
      return SimpleInformation.builder()
          .name(entrepreneur.getName())
          .businessNumber(entrepreneur.getBusinessNumber())
          .image(entrepreneur.getImage())
          .build();
    }
  }
}
