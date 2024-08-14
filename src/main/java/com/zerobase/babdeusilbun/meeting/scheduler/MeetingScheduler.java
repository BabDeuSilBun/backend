package com.zerobase.babdeusilbun.meeting.scheduler;

import com.zerobase.babdeusilbun.domain.Meeting;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MeetingScheduler {

  private final TaskScheduler taskScheduler;
  private final Map<String, ScheduledFuture<?>> meetingSchedulerMap = new ConcurrentHashMap<>();

  private final MeetingSchedulerService meetingSchedulerService;

  public void enrollMeetingSchedule(Meeting meeting) {

    ScheduledFuture<?> scheduledTask = taskScheduler.schedule(
        meetingSchedulerService.enrollSendPurchase(meeting.getId()),
        meeting.getPaymentAvailableDt().atZone(ZoneId.systemDefault()).toInstant());

    meetingSchedulerMap.put(getMeetingSchedulerMapId(meeting), scheduledTask);
  }

  public void deleteMeetingSchedule(Meeting meeting) {
    ScheduledFuture<?> scheduledTask = meetingSchedulerMap.remove(getMeetingSchedulerMapId(meeting));
    if (scheduledTask != null) {
      scheduledTask.cancel(false);
    } else {
      log.error("No task found with ID: {}", getMeetingSchedulerMapId(meeting));
    }
  }


  private String getMeetingSchedulerMapId(Meeting meeting) {
    return "meeting" + meeting.getId();
  }



}
