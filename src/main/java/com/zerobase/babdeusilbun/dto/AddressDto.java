package com.zerobase.babdeusilbun.dto;

import com.zerobase.babdeusilbun.domain.Address;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class AddressDto {

  private String postal;
  private String streetAddress;
  private String detailAddress;

  public static AddressDto fromEntity(Address address) {
    if (address == null) return AddressDto.builder()
        .build();

    return AddressDto.builder()
        .postal(address.getPostal())
        .streetAddress(address.getStreetAddress())
        .detailAddress(address.getDetailAddress())
        .build();
  }

  public Address toEntity() {
    return Address.builder()
        .postal(postal)
        .streetAddress(streetAddress)
        .detailAddress(detailAddress)
        .build();
  }
}
