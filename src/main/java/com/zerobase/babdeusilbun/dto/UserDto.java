package com.zerobase.babdeusilbun.dto;

import com.zerobase.babdeusilbun.domain.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.zerobase.babdeusilbun.domain.Address;

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
