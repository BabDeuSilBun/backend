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
 * 개인 주문 스냅샷
 */
@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
public class IndividualPurchasePayment extends BaseEntity{

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "snapshot_id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "individual_purchase_id", nullable = false)
  private IndividualPurchase individualPurchase;

  @Column(nullable = false)
  private Long menuId;

  @Column(nullable = false)
  private String menuName;

  @Column(nullable = false)
  private String image;

  @Column(nullable = false)
  private String menuDescription;

  @Column(nullable = false)
  private Long menuPrice;

  @Column(nullable = false)
  private Integer quantity;

}
