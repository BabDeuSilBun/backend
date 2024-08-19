package com.zerobase.babdeusilbun.domain;


import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 매장
 */
@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
public class Store extends BaseEntity{

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "store_id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "entrepreneur_id", nullable = false)
  private Entrepreneur entrepreneur;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private Long minPurchaseAmount;

  @Column(nullable = false)
  private Long deliveryPrice;

  @Column(nullable = false)
  private Integer minDeliveryTime;
  @Column(nullable = false)
  private Integer maxDeliveryTime;


  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "postal", column = @Column(nullable = false)),
      @AttributeOverride(name = "streetAddress", column = @Column(nullable = false)),
      @AttributeOverride(name = "detailAddress", column = @Column(nullable = false))
  })
  private Address address;

  @Column(nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private LocalTime openTime;
  @Column(nullable = false)
  private LocalTime closeTime;

  private LocalDateTime deletedAt;
}
