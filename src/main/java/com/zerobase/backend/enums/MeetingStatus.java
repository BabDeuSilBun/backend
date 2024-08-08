package com.zerobase.backend.enums;

import lombok.Getter;


@Getter
public enum MeetingStatus {

  GATHERING(Status.PROGRESS, "모집중"),
  ORDER_COMPLETED(Status.PROGRESS, "주문 완료"),
  ORDER_CANCELLED(Status.PROGRESS, "주문 취소"),
  COOKING(Status.PROGRESS, "조리중"),
  COOKING_COMPLETED(Status.PROGRESS, "조리 완료"),
  IN_DELIVERY(Status.PROGRESS, "배송중"),
  DELIVERY_COMPLETED(Status.PROGRESS, "배송 완료"),
  MEETING_CANCELLED(Status.CLOSED,"모임 취소"),
  MEETING_COMPLETED(Status.CLOSED, "모임 완료");

  private final Status status;
  private final String description;

  private enum Status {
    PROGRESS, CLOSED
  }

  MeetingStatus(Status status, String description) {
    this.status = status;
    this.description = description;
  }

  public boolean isProgress() {
    return this.getStatus() == Status.PROGRESS;
  }
}
