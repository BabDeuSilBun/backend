package com.zerobase.babdeusilbun.controller.meeting;

import static com.zerobase.babdeusilbun.dto.MeetingRequest.Create;
import static com.zerobase.babdeusilbun.dto.MeetingRequest.Update;
import static com.zerobase.babdeusilbun.swagger.annotation.meeting.MeetingBasicSwagger.CreateMeetingSwagger;
import static com.zerobase.babdeusilbun.swagger.annotation.meeting.MeetingBasicSwagger.GetAllMeetingListSwagger;
import static com.zerobase.babdeusilbun.swagger.annotation.meeting.MeetingBasicSwagger.UpdateMeetingInfoSwagger;
import static com.zerobase.babdeusilbun.swagger.annotation.meeting.MeetingBasicSwagger.WithdrawMeetingSwagger;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.zerobase.babdeusilbun.dto.MeetingDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.MeetingService;
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
@RequestMapping("/api/users/meetings")
@RequiredArgsConstructor
public class MeetingBasicController {

  private final MeetingService meetingService;

  /**
   * 모임리스트 목록 조회/검색 api
   * 정렬: 결제 마감 시간 순, 배송시간 짧은 순, 배달비 적은 순, 최소주문금액 낮은 순
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping
  @GetAllMeetingListSwagger
  public ResponseEntity<Page<MeetingDto>> getAllMeetingList(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestParam Long schoolId,
      @RequestParam String sortCriteria,
      @RequestParam String searchMenu,
      @RequestParam Long categoryFilter,
      Pageable pageable
  ) {

    return ResponseEntity.ok(
        meetingService.getAllMeetingDtoList
            (user.getId(), schoolId, sortCriteria, searchMenu, categoryFilter, pageable)
    );
  }

  /**
   * 모임 생성 api
   */
  @PreAuthorize("hasRole('USER')")
  @PostMapping
  @CreateMeetingSwagger
  public ResponseEntity<Void> createMeeting(
      @Validated @RequestBody Create request,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    // TODO
    //  userId
    meetingService.createMeeting(request, userDetails);
    return ResponseEntity.status(CREATED).build();
  }

  /**
   * 가게 주문 전 모임 정보 수정 api
   */
  @PreAuthorize("hasRole('USER')")
  @PostMapping("/{meetingId}")
  @UpdateMeetingInfoSwagger
  public ResponseEntity<Void> updateMeetingInfo(
      @PathVariable Long meetingId,
      @Validated @RequestBody Update request,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    // TODO
    //  userId
    meetingService.updateMeeting(meetingId, request, userDetails);

    return ResponseEntity.status(OK).build();
  }

  /**
   * 모임 탈퇴/취소 api
   */
  @PreAuthorize("hasRole('USER')")
  @DeleteMapping("/{meetingId}")
  @WithdrawMeetingSwagger
  public ResponseEntity<Void> withdrawMeeting(
      @PathVariable Long meetingId,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {

    // TODO
    //  userId
    meetingService.withdrawMeeting(meetingId, userDetails);

    return ResponseEntity.status(OK).build();
  }

}
