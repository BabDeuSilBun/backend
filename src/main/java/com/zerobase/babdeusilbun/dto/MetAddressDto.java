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
public class MetAddressDto {

  private String metPostal;
  private String metStreetAddress;
  private String metDetailAddress;

  public static MetAddressDto fromEntity(Address address) {
    return MetAddressDto.builder()
        .metPostal(address.getPostal())
        .metDetailAddress(address.getStreetAddress())
        .metStreetAddress(address.getDetailAddress())
        .build();
  }
}
