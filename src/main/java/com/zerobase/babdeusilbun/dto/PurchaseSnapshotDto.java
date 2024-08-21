package com.zerobase.babdeusilbun.dto;

import com.zerobase.babdeusilbun.domain.TeamPurchasePayment;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PurchaseSnapshotDto {

  private Long menuId;
  private String name;
  private String image;
  private String description;
  private Long price;
  private Integer quantity;
  private LocalDateTime createAt;
  private LocalDateTime updateAt;

  public static PurchaseSnapshotDto fromTeamPurchasePayment
      (TeamPurchasePayment teamPurchasePayment) {

    return PurchaseSnapshotDto.builder()
        .menuId(teamPurchasePayment.getMenuId())
        .image(teamPurchasePayment.getImage())
        .description(teamPurchasePayment.getMenuDescription())
        .price(teamPurchasePayment.getMenuPrice())
        .quantity(teamPurchasePayment.getQuantity())
        .createAt(teamPurchasePayment.getCreatedAt())
        .updateAt(teamPurchasePayment.getUpdatedAt())
        .build();
  }

}
