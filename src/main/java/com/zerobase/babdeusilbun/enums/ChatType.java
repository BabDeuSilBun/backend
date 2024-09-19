package com.zerobase.babdeusilbun.enums;

import com.zerobase.babdeusilbun.domain.User;
import lombok.Getter;

@Getter
public enum ChatType {

  ENTER("처음 들어갔을 때", "님이 입장하셨습니다."),
  CHAT("일반 채팅", null),
  LEAVE("채팅방을 떠났을 때", "님이 퇴장하셨습니다.");

  private final String description;
  private final String staticComment;

  ChatType(String description, String staticComment) {
    this.description = description;
    this.staticComment = staticComment;
  }

  public String getComment(User user) {
    return (staticComment != null) ? user.getNickname() + staticComment : "";
  }
}
