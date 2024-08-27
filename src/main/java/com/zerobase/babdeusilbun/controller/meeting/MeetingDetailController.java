package com.zerobase.babdeusilbun.controller.meeting;

import static com.zerobase.babdeusilbun.swagger.annotation.meeting.MeetingDetailSwagger.*;

import com.zerobase.babdeusilbun.dto.MeetingDto;
import com.zerobase.babdeusilbun.dto.MeetingHeadCountDto;
import com.zerobase.babdeusilbun.dto.MeetingUserDto;
import com.zerobase.babdeusilbun.service.MeetingService;
import com.zerobase.babdeusilbun.swagger.annotation.meeting.MeetingDetailSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/meetings/{meetingId}")
@RequiredArgsConstructor
public class MeetingDetailController {

  private final MeetingService meetingService;

  /**
   * 모임 정보 조회 api
   */
  @GetMapping
  @GetMeetingInfoSwagger
  public ResponseEntity<MeetingDto> getMeetingInfo(@PathVariable Long meetingId) {

    return ResponseEntity.ok(
        meetingService.getMeetingInfoDto(meetingId)
    );
  }

  /**
   * 모임장 조회 api
   */
  @GetMapping("/owner")
  @GetMeetingLeaderInfoSwagger
  public ResponseEntity<MeetingUserDto> getMeetingLeaderInfo(
      @PathVariable Long meetingId
  ) {

    return ResponseEntity.ok(
        MeetingUserDto.fromEntity(meetingService.getMeetingLeaderInfo(meetingId))
    );
  }

  /**
   * 모임원 조회 api
   */
  @GetMapping("/participant")
  @GetMeetingParticipantInfoSwagger
  public ResponseEntity<Page<MeetingUserDto>> getMeetingParticipantInfo(
      @PathVariable Long meetingId, Pageable pageable
  ) {

    return ResponseEntity.ok(
        meetingService.getMeetingParticipants(meetingId, pageable).map(MeetingUserDto::fromEntity)
    );
  }

  /**
   * 모임 현재 참가자 수 조회
   */
  @GetMapping("/headcount")
  @GetMeetingHeadCount
  public ResponseEntity<MeetingHeadCountDto> getMeetingHeadCount(@PathVariable Long meetingId) {

    return ResponseEntity.ok(
        MeetingHeadCountDto.builder()
            .headcount(meetingService.getMeetingHeadCount(meetingId))
            .build());
  }

}
