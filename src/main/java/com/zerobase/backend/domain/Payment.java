package com.zerobase.backend.domain;


import com.zerobase.backend.enums.PaymentMethod;
import com.zerobase.backend.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 결제 스냅샷
 */
@Entity(name = "payment") @Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
public class Payment {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payment_id", nullable = false)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @Column(name = "order_id", nullable = false)
  private Order order;

  @Column(nullable = false)
  private String portoneUid;

  @Column(nullable = false)
  private Long amount;

  @Column(nullable = false)
  private String pg;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private PaymentMethod method;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private PaymentStatus status;

}
