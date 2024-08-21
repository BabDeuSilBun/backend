package com.zerobase.babdeusilbun.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문 스냅샷
 */
@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
public class PurchasePayment extends BaseEntity{

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "purchase_payment_id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "purchase_id", nullable = false)
  private Purchase purchase;

  @Column(nullable = false)
  private Long deliveryPrice;
  @Column(nullable = false)
  private Long deliveryFee;

  private Long teamPurchasePrice;
  private Long teamPurchaseFee;

  private Long individualOrderPrice;

  @Column(nullable = false)
  private Long point;

}
