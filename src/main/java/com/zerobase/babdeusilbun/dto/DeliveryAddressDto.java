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
public class DeliveryAddressDto {

  private String deliveryPostal;
  private String deliveryStreetAddress;
  private String deliveryDetailAddress;

  public static DeliveryAddressDto fromEntity(Address address) {
    return DeliveryAddressDto.builder()
        .deliveryPostal(address.getPostal())
        .deliveryStreetAddress(address.getStreetAddress())
        .deliveryDetailAddress(address.getDetailAddress())
        .build();
  }

  public Address toAddressEntity() {
    return Address.builder()
        .postal(deliveryPostal)
        .streetAddress(deliveryStreetAddress)
        .detailAddress(deliveryDetailAddress)
        .build();
  }
}
