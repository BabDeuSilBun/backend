package com.zerobase.babdeusilbun.controller;

import static org.springframework.http.HttpStatus.*;

import com.zerobase.babdeusilbun.dto.MeetingDto;
import com.zerobase.babdeusilbun.dto.MeetingHeadCountDto;
import com.zerobase.babdeusilbun.dto.MeetingUserDto;
import com.zerobase.babdeusilbun.dto.MeetingRequest;
import com.zerobase.babdeusilbun.service.MeetingService;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MeetingController {

  private final MeetingService meetingService;


  // 모임리스트 목록 조회/검색 api
  // 정렬: 결제 마감 시간 순, 배송시간 짧은 순, 배달비 적은 순, 최소주문금액 낮은 순
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/users/meetings")
  public ResponseEntity<Page<MeetingDto>> getAllMeetingList(
      @RequestParam Long schoolId,
      @RequestParam String sortCriteria,
      @RequestParam String searchMenu,
      @RequestParam Long categoryFilter,
      Pageable pageable
  ) {

    return ResponseEntity.ok(
        meetingService.getAllMeetingDtoList(schoolId, sortCriteria, searchMenu, categoryFilter,
            pageable)
    );
  }


  // 모임 정보 조회 api
  @GetMapping("/users/meetings/{meetingId}")
  public ResponseEntity<MeetingDto> getMeetingInfo(@PathVariable Long meetingId) {

    return ResponseEntity.ok(
        meetingService.getMeetingInfoDto(meetingId)
    );
  }

  // 모임 생성 api
  @PreAuthorize("hasRole('USER')")
  @PostMapping("/users/meetings")
  public ResponseEntity<Void> createMeeting(
      @Validated @RequestBody MeetingRequest.Create request,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    meetingService.createMeeting(request, userDetails);
    return ResponseEntity.status(CREATED).build();
  }

  // 가게 주문 전 모임 정보 수정 api
  @PreAuthorize("hasRole('USER')")
  @PostMapping("/users/meetings/{meetingId}")
  public ResponseEntity<Void> updateMeetingInfo(
      @PathVariable Long meetingId,
      @Validated @RequestBody MeetingRequest.Update request,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    meetingService.updateMeeting(meetingId, request, userDetails);

    return ResponseEntity.status(OK).build();
  }

  // 모임 탈퇴/취소 api
  @PreAuthorize("hasRole('USER')")
  @DeleteMapping("/users/meetings/{meetingId}")
  public ResponseEntity<Void> withdrawMeeting(
      @PathVariable Long meetingId,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {

    meetingService.withdrawMeeting(meetingId, userDetails);

    return ResponseEntity.status(OK).build();
  }

  // 모임장 조회 api
  @GetMapping("/users/meetings/{meetingId}/owner")
  public ResponseEntity<MeetingUserDto> getMeetingLeaderInfo(
      @PathVariable Long meetingId
  ) {

    return ResponseEntity.ok(
        MeetingUserDto.fromEntity(meetingService.getMeetingLeaderInfo(meetingId))
    );
  }

  // 모임원 조회 api
  @GetMapping("/users/meetings/{meetingId}/participant")
  public ResponseEntity<Page<MeetingUserDto>> getMeetingParticipantInfo(
      @PathVariable Long meetingId, Pageable pageable
  ) {

    return ResponseEntity.ok(
        meetingService.getMeetingParticipants(meetingId, pageable).map(MeetingUserDto::fromEntity)
    );
  }

  // 모임 현재 참가자 수 조회 /api/users/meetings/{meetingId}/headcount
  @GetMapping("/users/meetings/{meetingId}/headcount")
  public ResponseEntity<MeetingHeadCountDto> getMeetingHeadCount(@PathVariable Long meetingId) {

    return ResponseEntity.ok(
        MeetingHeadCountDto.builder()
            .headcount(meetingService.getMeetingHeadCount(meetingId))
            .build());
  }

}
