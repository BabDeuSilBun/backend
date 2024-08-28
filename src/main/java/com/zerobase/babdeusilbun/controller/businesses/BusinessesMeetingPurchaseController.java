package com.zerobase.babdeusilbun.controller.businesses;

import com.zerobase.babdeusilbun.dto.ChatDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/businesses/meetings")
@RequiredArgsConstructor
public class BusinessesMeetingPurchaseController {
  private final MeetingService meetingService;

  /**
   * 모임 주문 승인
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @PostMapping("/{meetingId}/confirm")
  public ResponseEntity<Void> confirmMeetingPurchaseByMeetingId(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("meetingId") Long meetingId
  ) {
    meetingService.confirmMeetingPurchase(entrepreneur.getId(), meetingId);

    return ResponseEntity.ok().build();
  }

  /**
   * 모임 주문 거절
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @PostMapping("/{meetingId}/deny")
  public ResponseEntity<Void> denyMeetingPurchases(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("meetingId") Long meetingId
  ) {
    meetingService.denyMeetingPurchase(entrepreneur.getId(), meetingId);

    return ResponseEntity.ok().build();
  }

  /**
   * 조리 완료
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @PostMapping("/{meetingId}/complete")
  public ResponseEntity<Void> completeMeetingPurchases(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("meetingId") Long meetingId
  ) {
    meetingService.completeMeetingPurchase(entrepreneur.getId(), meetingId);

    return ResponseEntity.ok().build();
  }

  /**
   * 지연 사유 작성
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @PostMapping("/{meetingId}/delay/details")
  public ResponseEntity<Void> sendMessageForDelayMeetingPurchases(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("meetingId") Long meetingId,
      @RequestBody ChatDto.Request request
  ) {
    meetingService.sendMessageForDelayMeetingPurchases(entrepreneur.getId(), meetingId, request);

    return ResponseEntity.ok().build();
  }
}
