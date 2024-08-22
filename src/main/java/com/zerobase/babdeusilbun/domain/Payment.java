package com.zerobase.babdeusilbun.domain;


import com.zerobase.babdeusilbun.enums.PaymentGateway;
import com.zerobase.babdeusilbun.enums.PaymentMethod;
import com.zerobase.babdeusilbun.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 결제 스냅샷
 */
@Entity
@Getter
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

  @Column(unique = true, nullable = false)
  private String transactionId;

  @Column(nullable = false)
  private String portoneUid;

  @Column(nullable = false)
  private Long amount;

  @Column(nullable = false)
  private PaymentGateway pg;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private PaymentMethod method;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private PaymentStatus status;

}
