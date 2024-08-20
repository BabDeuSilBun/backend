package com.zerobase.babdeusilbun.domain;


import com.zerobase.babdeusilbun.dto.EntrepreneurDto;
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
@Entity
@Getter
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


  public void update(EntrepreneurDto.UpdateRequest request) {
    if (request.getPhoneNumber() != null) this.phoneNumber = request.getPhoneNumber();
    if(request.getPostal() != null && request.getStreetAddress() != null && request.getDetailAddress() != null) {
      this.address = address.builder()
              .postal(request.getPostal())
              .streetAddress(request.getStreetAddress())
              .detailAddress(request.getDetailAddress())
              .build();
    }
    if (request.getPassword() != null) this.password = request.getPassword();
    if (request.getImage() != null) this.image = request.getImage();

  }
}
