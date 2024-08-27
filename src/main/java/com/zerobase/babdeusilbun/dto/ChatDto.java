package com.zerobase.babdeusilbun.dto;

import com.zerobase.babdeusilbun.domain.Chat;
import com.zerobase.babdeusilbun.enums.ChatType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

public class ChatDto {
  @Data
  @Builder
  @NoArgsConstructor
  public static class Information {
    private Long senderId;
    private String nickname;
    private ChatType type;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Information fromEntity(Chat chat) {
      return Information.builder()
          .senderId(chat.getUser().getId())
          .nickname(chat.getUser().getNickname())
          .type(chat.getType())
          .content(chat.getContent())
          .createdAt(chat.getCreatedAt())
          .updatedAt(chat.getUpdatedAt())
          .build();
    }

    public Information(Long senderId, String nickname, ChatType type,
        String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
      this.senderId = senderId;
      this.nickname = nickname;
      this.type = type;
      this.content = content;
      this.createdAt = createdAt;
      this.updatedAt = updatedAt;
    }
  }

  public interface RoomInformation {
    @Value("#{target.id}")
    Long getChatRoomId();
    @Value("#{'[' + target.meeting.purchaseType.getDescription() + '] ' + target.meeting.store.name}")
    String getName();
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Request {
    private String content;
  }
}
