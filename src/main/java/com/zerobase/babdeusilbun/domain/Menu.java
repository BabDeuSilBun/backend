package com.zerobase.babdeusilbun.domain;


import com.zerobase.babdeusilbun.dto.MenuDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;

import lombok.*;

/**
 * 메뉴
 */
@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
@Table(name = "menu",
    // 가게에서 같은 메뉴(메뉴 이름과 가격이 동일)는 등록하지 못함
    uniqueConstraints = @UniqueConstraint(columnNames = {"store_id", "name", "price"})
)
public class Menu extends BaseEntity{

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "menu_id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  @Column(nullable = false)
  private String name;

  private String image;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private Long price;

  private LocalDateTime deletedAt;

  public void update(MenuDto.UpdateRequest request) {
    if(request.getName() != null) this.name = request.getName();
    if(request.getDescription() != null) this.description = request.getDescription();
    if(request.getImage() != null) this.image = request.getImage();
    if(request.getPrice() >= 0) this.price = request.getPrice();
  }

  public void delete() {
    deletedAt = LocalDateTime.now();
  }
}
