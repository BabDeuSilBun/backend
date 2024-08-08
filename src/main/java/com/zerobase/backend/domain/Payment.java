package com.zerobase.backend.domain;


import com.zerobase.backend.enums.PaymentMethod;
import com.zerobase.backend.enums.PaymentStatus;
import jakarta.persistence.*;
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
public class Payment extends BaseEntity{

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payment_id", nullable = false)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "purchase_id", nullable = false)
  private Purchase purchase;

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
