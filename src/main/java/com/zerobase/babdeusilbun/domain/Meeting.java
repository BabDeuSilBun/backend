package com.zerobase.babdeusilbun.domain;


import com.zerobase.babdeusilbun.enums.MeetingStatus;
import com.zerobase.babdeusilbun.enums.PurchaseType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 모임
 */
@Entity(name = "meeting") @Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
public class Meeting extends BaseEntity{

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "meeting_id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "leader_id", nullable = false)
  private User leader;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private PurchaseType purchaseType;

  @Column(nullable = false)
  private Integer minHeadcount;
  @Column(nullable = false)
  private Integer maxHeadcount;

  @Column(nullable = false)
  private Boolean isEarlyPaymentAvailable;

  @Column(nullable = false)
  private LocalDateTime paymentAvailableDt;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "postal", column = @Column(name = "delivered_postal", nullable = false)),
      @AttributeOverride(name = "streetAddress", column = @Column(name = "delivered_street_address", nullable = false)),
      @AttributeOverride(name = "detailAddress", column = @Column(name = "delivered_detail_address", nullable = false))
  })
  private Address deliveredAddress;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "postal", column = @Column(name = "met_postal", nullable = false)),
      @AttributeOverride(name = "streetAddress", column = @Column(name = "met_street_address", nullable = false)),
      @AttributeOverride(name = "detailAddress", column = @Column(name = "met_detail_address", nullable = false))
  })
  private Address metAddress;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private MeetingStatus status;

  private LocalDateTime deliveredAt;

  private LocalDateTime deletedAt;



}
