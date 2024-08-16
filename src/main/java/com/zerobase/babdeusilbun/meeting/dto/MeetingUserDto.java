package com.zerobase.babdeusilbun.meeting.dto;

import com.zerobase.babdeusilbun.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MeetingUserDto {

  private Long userId;
  private String nickName;
  private String image;

  public static MeetingUserDto fromEntity(User user) {
    return MeetingUserDto.builder()
        .userId(user.getId())
        .nickName(user.getNickname())
        .image(user.getImage())
        .build();
  }


}
