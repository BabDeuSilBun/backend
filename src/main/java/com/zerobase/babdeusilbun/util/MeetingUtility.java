package com.zerobase.babdeusilbun.util;

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
}
