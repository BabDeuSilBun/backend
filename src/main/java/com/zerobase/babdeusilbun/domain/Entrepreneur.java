package com.zerobase.babdeusilbun.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사업자
 */
@Entity @Getter
@Table(indexes = {
    @Index(name = "idx_entrepreneur_email", columnList = "email")
})
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
public class Entrepreneur extends BaseEntity{

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "entrepreneur_id", nullable = false)
  private Long id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  private String image;

  @Column(nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private String businessNumber;

  @Embedded
  private Address address;

  private LocalDateTime deletedAt;

  public void withdraw() {
    this.deletedAt = LocalDateTime.now();
  }


}
