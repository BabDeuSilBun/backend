package com.zerobase.babdeusilbun.dto;

import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PurchaseDto {

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder
  public static class TeamPurchaseResponse {

    private Long totalFee;
    private List<Item> items;

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
