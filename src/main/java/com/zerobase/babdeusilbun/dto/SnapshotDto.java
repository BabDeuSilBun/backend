package com.zerobase.babdeusilbun.dto;

import com.zerobase.babdeusilbun.domain.IndividualPurchasePayment;
import com.zerobase.babdeusilbun.domain.Payment;
import com.zerobase.babdeusilbun.domain.Point;
import com.zerobase.babdeusilbun.domain.PurchasePayment;
import com.zerobase.babdeusilbun.domain.TeamPurchasePayment;
import com.zerobase.babdeusilbun.enums.PaymentMethod;
import com.zerobase.babdeusilbun.enums.PaymentStatus;
import com.zerobase.babdeusilbun.enums.PointType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class SnapshotDto {

  /**
   * 주문 후 주문 스냅샷
   */
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder
  public static class PurchaseSnapshot {
    private Long deliveryPrice;
    private Long deliveryFee;
    private Long teamPurchasePrice;
    private Long teamPurchaseFee;
    private Long individualPurchase;
    private Long point;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public static SnapshotDto.PurchaseSnapshot fromSnapshotEntity(PurchasePayment purchasePayment) {
      return PurchaseSnapshot.builder()
          .deliveryPrice(purchasePayment.getDeliveryPrice())
          .deliveryFee(purchasePayment.getDeliveryFee())
          .teamPurchasePrice(purchasePayment.getTeamPurchasePrice())
          .teamPurchaseFee(purchasePayment.getTeamPurchaseFee())
          .point(purchasePayment.getPoint())
          .createAt(purchasePayment.getCreatedAt())
          .updateAt(purchasePayment.getUpdatedAt())
          .build();
    }
  }

  /**
   * 주문 후 개별, 그룹 결제 스냅샷
   */
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder
  public static class SubPurchaseSnapshot {
    private Long menuId;
    private String name;
    private String image;
    private String description;
    private Long price;
    private Integer quantity;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public static SnapshotDto.SubPurchaseSnapshot fromSnapshotEntity(TeamPurchasePayment teamPurchasePayment) {

      return SnapshotDto.SubPurchaseSnapshot.builder()
          .menuId(teamPurchasePayment.getMenuId())
          .image(teamPurchasePayment.getImage())
          .description(teamPurchasePayment.getMenuDescription())
          .price(teamPurchasePayment.getMenuPrice())
          .quantity(teamPurchasePayment.getQuantity())
          .createAt(teamPurchasePayment.getCreatedAt())
          .updateAt(teamPurchasePayment.getUpdatedAt())
          .build();
    }

    public static SnapshotDto.SubPurchaseSnapshot fromSnapshotEntity
        (IndividualPurchasePayment individualPurchasePayment) {

      return SnapshotDto.SubPurchaseSnapshot.builder()
          .menuId(individualPurchasePayment.getMenuId())
          .image(individualPurchasePayment.getImage())
          .description(individualPurchasePayment.getMenuDescription())
          .price(individualPurchasePayment.getMenuPrice())
          .quantity(individualPurchasePayment.getQuantity())
          .createAt(individualPurchasePayment.getCreatedAt())
          .updateAt(individualPurchasePayment.getUpdatedAt())
          .build();
    }
  }

  /**
   * 포인트 스냅샷
   */
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder
  public static class PointSnapshot {

    private Long snapshotId;
    private PointType type;
    private Long amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PointSnapshot fromPointEntity(Point point) {
      return PointSnapshot.builder()
          .snapshotId(point.getPurchasePayment().getId())
          .type(point.getType())
          .amount(point.getAmount())
          .createdAt(point.getCreatedAt())
          .updatedAt(point.getUpdatedAt())
          .build();
    }
  }

  /**
   * 결제 스냅샷
   */
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder
  public static class PaymentSnapshot {

    private String portoneUid;
    private Long amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PaymentSnapshot fromPaymentEntity(Payment payment) {
      return PaymentSnapshot.builder()
          .portoneUid(payment.getPortoneUid())
          .amount(payment.getAmount())
          .method(payment.getMethod())
          .status(payment.getStatus())
          .createdAt(payment.getCreatedAt())
          .updatedAt(payment.getUpdatedAt())
          .build();
    }

  }




}
