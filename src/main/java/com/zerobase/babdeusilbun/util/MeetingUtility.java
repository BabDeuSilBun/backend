package com.zerobase.babdeusilbun.util;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.enums.MeetingStatus;
import java.util.List;

public class MeetingUtility {
  public final static List<MeetingStatus> ENTREPRENEUR_CAN_CONFIRM_OR_DENY = List.of(
      MeetingStatus.PURCHASE_COMPLETED
  );
  public final static List<MeetingStatus> ENTREPRENEUR_CAN_COMPLETE = List.of(
      MeetingStatus.COOKING
  );
  public final static List<MeetingStatus> ENTREPRENEUR_CAN_SEND_DELAY_MESSAGE = List.of(
      MeetingStatus.COOKING, MeetingStatus.COOKING_COMPLETED
  );
  public final static List<MeetingStatus> CAN_ENTREPRENEUR_CHECK_PURCHASE_STATUS = List.of(
      MeetingStatus.PURCHASE_CANCELLED,
      MeetingStatus.PURCHASE_COMPLETED, MeetingStatus.COOKING, MeetingStatus.COOKING_COMPLETED,
      MeetingStatus.IN_DELIVERY, MeetingStatus.DELIVERY_COMPLETED
  );
  public final static List<MeetingStatus> CANCELED_STATUS = List.of(
      MeetingStatus.PURCHASE_CANCELLED
  );
  public final static List<MeetingStatus> AFTER_PAYMENT_STATUS = List.of(
      MeetingStatus.PURCHASE_COMPLETED, MeetingStatus.COOKING, MeetingStatus.COOKING_COMPLETED,
      MeetingStatus.IN_DELIVERY, MeetingStatus.DELIVERY_COMPLETED
  );

  public static String getTitle(Meeting meeting) {
    if (meeting == null) return "";

    return String.format("[%s] %s", meeting.getPurchaseType().getDescription(), meeting.getStore());
  }
}
