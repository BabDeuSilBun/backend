package com.zerobase.babdeusilbun.dto;

import com.zerobase.babdeusilbun.domain.Point;
import com.zerobase.babdeusilbun.enums.PointType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PointDto {

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder
  public static class WithdrawalRequest {
    private Integer amount;
  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder
  public static class Response {

    private String store;
    private PointType type;
    private String content;
    private Long amount;
    private LocalDateTime createdAt;

    public static Response fromEntity(Point point, String storeName) {
      return Response.builder()
          .store(storeName)
          .type(point.getType())
          .content(point.getContent())
          .amount(point.getAmount())
          .createdAt(point.getCreatedAt())
          .build();
    }
  }

}
