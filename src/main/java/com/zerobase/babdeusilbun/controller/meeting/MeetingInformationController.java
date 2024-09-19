package com.zerobase.babdeusilbun.controller.meeting;

import com.zerobase.babdeusilbun.dto.MeetingDto;
import com.zerobase.babdeusilbun.dto.MeetingHeadCountDto;
import com.zerobase.babdeusilbun.dto.MeetingUserDto;
import com.zerobase.babdeusilbun.service.MeetingService;
import com.zerobase.babdeusilbun.swagger.annotation.meeting.MeetingInformationSwagger.GetMeetingHeadCount;
import com.zerobase.babdeusilbun.swagger.annotation.meeting.MeetingInformationSwagger.GetMeetingInfoSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.meeting.MeetingInformationSwagger.GetMeetingLeaderInfoSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.meeting.MeetingInformationSwagger.GetMeetingParticipantInfoSwagger;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/meetings")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class MeetingInformationController {
  private final MeetingService meetingService;

  @GetMapping("/{meetingId}")
  @GetMeetingInfoSwagger
  public ResponseEntity<MeetingDto> getMeetingInfo(
      @PathVariable("meetingId") Long meetingId
  ) {
    return ResponseEntity.ok(
        meetingService.getMeetingInfoDto(meetingId)
    );
  }

  @GetMapping("/{meetingId}/owner")
  @GetMeetingLeaderInfoSwagger
  public ResponseEntity<MeetingUserDto> getMeetingLeaderInfo(
      @PathVariable("meetingId") Long meetingId
  ) {
    return ResponseEntity.ok(
        MeetingUserDto.fromEntity(meetingService.getMeetingLeaderInfo(meetingId))
    );
  }

  @GetMapping("/{meetingId}/participant")
  @GetMeetingParticipantInfoSwagger
  public ResponseEntity<Page<MeetingUserDto>> getMeetingParticipantInfo(
      @PathVariable("meetingId") Long meetingId,
      @Parameter(description = "모임원 정보 목록에서 보일 페이지번호와 한 페이지당 보이는 항목 개수")
      Pageable pageable
  ) {
    return ResponseEntity.ok(
        meetingService.getMeetingParticipants(meetingId, pageable).map(MeetingUserDto::fromEntity)
    );
  }

  @GetMapping("/{meetingId}/headcount")
  @GetMeetingHeadCount
  public ResponseEntity<MeetingHeadCountDto> getMeetingHeadCount(
      @PathVariable("meetingId") Long meetingId
  ) {
    return ResponseEntity.ok(
        MeetingHeadCountDto.builder()
            .headcount(meetingService.getMeetingHeadCount(meetingId))
            .build());
  }
}
