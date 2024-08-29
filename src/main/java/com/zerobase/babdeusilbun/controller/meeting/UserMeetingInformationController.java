package com.zerobase.babdeusilbun.controller.meeting;

import com.zerobase.babdeusilbun.dto.MeetingDto;
import com.zerobase.babdeusilbun.dto.PurchaseDto.DeliveryFeeResponse;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.MeetingService;
import com.zerobase.babdeusilbun.service.PurchaseService;
import com.zerobase.babdeusilbun.swagger.annotation.meeting.UserMeetingInformationSwagger.GetAllMeetingListSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.meeting.UserMeetingInformationSwagger.GetDeliveryFeeInfoSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/meetings")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserMeetingInformationController {
  private final MeetingService meetingService;
  private final PurchaseService purchaseService;

  @GetMapping
  @GetAllMeetingListSwagger
  public ResponseEntity<Page<MeetingDto>> getAllMeetingList(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestParam("schoolId") Long schoolId,
      @RequestParam("sortCriteria") String sortCriteria,
      @RequestParam("searchMenu") String searchMenu,
      @RequestParam("categoryFilter") Long categoryFilter,
      @Parameter(description = "모임리스트 목록의 페이지 번호와 한 페이지당 보일 항목 개수 설정")
      Pageable pageable
  ) {
    return ResponseEntity.ok(
        meetingService.getAllMeetingDtoList
            (user.getId(), schoolId, sortCriteria, searchMenu, categoryFilter, pageable)
    );
  }

  /**
   * 주문 전 모임 배달비 조회
   */
  @GetMapping("/{meetingId}/delivery-fee")
  @GetDeliveryFeeInfoSwagger
  public ResponseEntity<DeliveryFeeResponse> getDeliveryFeeInfo(
      @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long meetingId
  ) {

    return ResponseEntity.ok(purchaseService.getDeliveryFeeInfo(userDetails.getId(), meetingId));
  }
}
