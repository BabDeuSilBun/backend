package com.zerobase.babdeusilbun.enums;

import lombok.Getter;

@Getter
public enum ChatType {

  ENTER("처음 들어갔을 때"),
  CHAT("일반 채팅");

  private final String description;

  ChatType(String description) {
    this.description = description;
  }
}
