package com.zerobase.backend.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 개인 주문
 */
@Entity(name = "individual_order") @Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
public class IndividualOrder extends BaseEntity{

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "individual_order_id", nullable = false)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "menu_id", nullable = false)
  private Menu menu;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @Column(nullable = false)
  private Integer quantity;

}
