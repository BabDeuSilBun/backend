package com.zerobase.babdeusilbun.dto;

import com.zerobase.babdeusilbun.domain.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
  @Builder
  public static class Profile {
    String nickname;
    String image;
    String major;
    Long meetingCount;
    List<EvaluateDto.PositiveEvaluate> positiveEvaluate;
  }

  public interface MyPage {
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
