package com.zerobase.backend.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 메뉴
 */
@Entity @Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
@Table(name = "menu",
    // 가게에서 같은 메뉴(메뉴 이름과 가격이 동일)는 등록하지 못함
    uniqueConstraints = @UniqueConstraint(columnNames = {"store_id", "name", "price"})
)
public class Menu extends BaseEntity{

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "menu_id", nullable = false)
  private Long id;

  @Column(nullable = false)
  private String name;

  private String image;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private Long price;

  private LocalDateTime deletedAt;

}
