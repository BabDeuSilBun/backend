package com.zerobase.babdeusilbun.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.enums.MeetingStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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


  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder
  public static class DeliveryFeeResponse {
    private Long price;
    private Long fee;
  }

  @Data
  @Builder
  public static class MeetingPurchaseResponse {
    private Long meetingId;
    private Page<MenuResponse> menu;
    private AddressDto address;
    private MeetingStatus status;

    public static MeetingPurchaseResponse fromEntity(Meeting meeting, Page<MenuResponse> menu) {
      return MeetingPurchaseResponse.builder()
          .meetingId(meeting.getId())
          .menu(menu)
          .address(AddressDto.fromEntity(meeting.getDeliveredAddress()))
          .status(meeting.getStatus())
          .build();
    }
  }

  @Data
  @Builder
  public static class MenuResponse {
    private Long menuId;
    private String name;
    private int quantity;

    @QueryProjection
    public MenuResponse(Long menuId, String name, int quantity) {
      this.menuId = menuId;
      this.name = name;
      this.quantity = quantity;
    }
  }
}
