package com.zerobase.babdeusilbun.dto;

import com.zerobase.babdeusilbun.domain.Address;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AddressDto {

  private String postal;
  private String streetAddress;
  private String detailAddress;

  public static AddressDto fromEntity(Address address) {
    return AddressDto.builder()
        .postal(address.getPostal())
        .streetAddress(address.getStreetAddress())
        .detailAddress(address.getDetailAddress())
        .build();
  }

}
