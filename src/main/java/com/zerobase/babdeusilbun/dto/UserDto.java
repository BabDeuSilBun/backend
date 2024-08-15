package com.zerobase.babdeusilbun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
