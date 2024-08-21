package com.zerobase.babdeusilbun.util;

import com.zerobase.babdeusilbun.domain.Address;
import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;

public class TestEntrepreneurUtility {
  private final static Entrepreneur entrepreneur = Entrepreneur.builder()
      .id(1L)
      .email("entrepreneur@test.com")
      .password("password")
      .name("가짜 사업가")
      .image("가짜 이미지")
      .phoneNumber("00000000000")
      .businessNumber("0000000000")
      .address(Address.builder()
          .postal("00000")
          .streetAddress("도로명주소")
          .detailAddress("상세주소")
          .build())
      .build();

  public static CustomUserDetails createTestEntrepreneur() {
    return new CustomUserDetails(entrepreneur);
  }

  public static Entrepreneur getEntrepreneur() {
    return entrepreneur;
  }
}
