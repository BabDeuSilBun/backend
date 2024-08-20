package com.zerobase.babdeusilbun.dto;

import com.zerobase.babdeusilbun.domain.BankAccount;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import com.zerobase.babdeusilbun.domain.Address;

import java.util.List;

public class UserDto {
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UpdateRequest {
    private String nickname;
    private String password;
    private String image;
    private String phoneNumber;
    private Long schoolId;
    private Long majorId;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UpdateAddress {

    @NotBlank(message = "postal 항목은 빈값이 올 수 없습니다.")
    private String postal;

    @NotBlank(message = "streetAddress 항목은 빈값이 올 수 없습니다.")
    private String streetAddress;

    @NotBlank(message = "detailAddress 항목은 빈값이 올 수 없습니다.")
    private String detailAddress;
  }

  @Data
  @Builder
  public static class Profile {
    String nickname;
    String image;
    String major;
    Long meetingCount;
    List<EvaluateDto.PositiveEvaluate> positiveEvaluate;
  }

  public interface MyPage {
    Long getUserId();
    String getName();
    String getNickname();
    String getPhoneNumber();
    String getEmail();

    Long getPoint();
    String getImage();
    Long getMeetingCount();
    String getSchool();
    String getCampus();
    String getMajor();
    Boolean getIsBanned();

    Address getAddress();

    BankAccount getBankAccount();

  }
}
