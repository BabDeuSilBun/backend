package com.zerobase.babdeusilbun.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SignRequest {

  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Getter @Builder
  public static class SignIn {
    @Email
    @NotBlank(message = "email 항목은 빈값이 올 수 없습니다.")
    private String email;
    @NotBlank(message = "password 항목은 빈값이 올 수 없습니다.")
    private String password;
  }

  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Getter @Builder
  public static class UserSignUp {

    @NotNull(message = "schoolId 항목은 Null 값이 올 수 없습니다.")
    private Long schoolId;

    @NotNull(message = "majorId 항목은 Null 값이 올 수 없습니다.")
    private Long majorId;

    @Email
    @NotBlank(message = "email 항목은 빈값이 올 수 없습니다.")
    private String email;

    @NotBlank(message = "password 항목은 빈값이 올 수 없습니다.")
    private String password;

    @NotBlank(message = "name 항목은 빈값이 올 수 없습니다.")
    private String name;

    @NotBlank(message = "nickname 항목은 빈값이 올 수 없습니다.")
    private String nickname;

    @NotBlank(message = "phoneNumber 항목은 빈값이 올 수 없습니다.")
    private String phoneNumber;

    private Address address;

  }

  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Getter @Builder
  public static class BusinessSignUp {

    @Email
    @NotBlank(message = "email 항목은 빈값이 올 수 없습니다.")
    private String email;

    @NotBlank(message = "password 항목은 빈값이 올 수 없습니다.")
    private String password;

    @NotBlank(message = "name 항목은 빈값이 올 수 없습니다.")
    private String name;

    @NotBlank(message = "phoneNumber 항목은 빈값이 올 수 없습니다.")
    private String phoneNumber;

    @NotBlank(message = "businessNumber 항목은 빈값이 올 수 없습니다.")
    private String businessNumber;

    private Address address;
  }

  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Getter @Builder
  public static class Address {

    @NotBlank(message = "postal 항목은 빈값이 올 수 없습니다.")
    private String postal;

    @NotBlank(message = "streetAddress 항목은 빈값이 올 수 없습니다.")
    private String streetAddress;

    @NotBlank(message = "detailAddress 항목은 빈값이 올 수 없습니다.")
    private String detailAddress;


  }
}
