package com.zerobase.babdeusilbun.scheduler;

import static com.zerobase.babdeusilbun.exception.ErrorCode.*;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingSchedulerService {

  private final MeetingRepository meetingRepository;

  public Runnable enrollSendPurchase(Long meetingId) {

    Meeting findMeeting = meetingRepository.findById(meetingId)
        .orElseThrow(() -> new CustomException(MEETING_NOT_FOUND));

    return () -> {
      // meeting status 변경
      findMeeting.completeDeadline();

      //TODO
      // 상점에게 주문 보내기
    };
  }

}
