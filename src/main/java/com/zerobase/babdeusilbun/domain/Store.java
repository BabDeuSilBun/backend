package com.zerobase.babdeusilbun.domain;


import com.zerobase.babdeusilbun.dto.StoreDto;
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
@Entity(name = "store") @Getter
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

  public void update(StoreDto.UpdateRequest request) {
    if (request.getName() != null) {
      this.name = request.getName();
    }
    if (request.getMinPurchasePrice() != null) {
      this.minPurchaseAmount = request.getMinPurchasePrice();
    }
    if (request.getMinDeliveryTime() != null) {
      this.minDeliveryTime = request.getMinDeliveryTime();
    }
    if (request.getMaxDeliveryTime() != null) {
      this.maxDeliveryTime = request.getMaxDeliveryTime();
    }
    if (request.getDeliveryPrice() != null) {
      this.deliveryPrice = request.getDeliveryPrice();
    }
    if (request.getAddress() != null) {
      this.address = request.getAddress().toEntity();
    }
    if (request.getPhoneNumber() != null) {
      this.phoneNumber = request.getPhoneNumber();
    }
    if (request.getOpenTime() != null) {
      this.openTime = request.getOpenTime();
    }
    if (request.getCloseTime() != null) {
      this.closeTime = request.getCloseTime();
    }
  }
}
