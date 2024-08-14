package com.zerobase.babdeusilbun.meeting.controller;

import static org.springframework.http.HttpStatus.*;

import com.zerobase.babdeusilbun.dto.MeetingDto;
import com.zerobase.babdeusilbun.meeting.dto.MeetingLeaderDto;
import com.zerobase.babdeusilbun.meeting.dto.MeetingRequest;
import com.zerobase.babdeusilbun.meeting.service.MeetingService;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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
  public ResponseEntity<?> getAllMeetingList(
      @RequestParam Long schoolId,
      @RequestParam String sortCriteria,
      @RequestParam String searchMenu,
      @RequestParam Long categoryFilter,
      Pageable pageable
  ) {

    return ResponseEntity.ok(
        meetingService.getAllMeetingList(schoolId, sortCriteria, searchMenu, categoryFilter, pageable)
    );
  }


  // 모임 정보 조회 api
  @GetMapping("/users/meetings/{meetingId}")
  public ResponseEntity<?> getMeetingInfo(@PathVariable Long meetingId) {

    return ResponseEntity.ok(
        meetingService.getMeetingInfo(meetingId)
    );
  }

  // 모임 생성 api
  @PreAuthorize("hasRole('USER')")
  @PostMapping("/users/meetings")
  public ResponseEntity<?> createMeeting(
      @Validated @RequestBody MeetingRequest.Create request,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    meetingService.createMeeting(request, userDetails);
    return ResponseEntity.status(CREATED).build();
  }

  // 가게 주문 전 모임 정보 수정 api
  @PreAuthorize("hasRole('USER')")
  @PostMapping("/users/meetings/{meetingId}")
  public ResponseEntity<?> updateMeetingInfo(
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
  public ResponseEntity<?> withdrawMeeting(
      @PathVariable Long meetingId,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {

    meetingService.withdrawMeeting(meetingId, userDetails);

    return ResponseEntity.status(OK).build();
  }

  // 모임장 조회
  @GetMapping("/users/meetings/{meetingId}/owner")
  public ResponseEntity<?> getMeetingLeaderInfo(
      @PathVariable Long meetingId
  ) {

    MeetingLeaderDto meetingLeaderDto = meetingService.getMeetingLeaderInfo(meetingId);

    return ResponseEntity.ok(null);
  }

}
