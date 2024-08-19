package com.zerobase.babdeusilbun.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

public class PurchaseDto {

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder
  public static class PurchaseResponse {

    private Long totalFee;
    private Page<Item> items;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class Item {

      private Long purchaseId;
      private Long menuId;
      private String name;
      private String image;
      private String description;
      private Long price;
      private Integer quantity;
    }

  }



}
