package com.zerobase.babdeusilbun.controller.businesses;

import com.zerobase.babdeusilbun.dto.PurchaseDto;
import com.zerobase.babdeusilbun.dto.PurchaseDto.MeetingPurchaseResponse;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.MeetingService;
import com.zerobase.babdeusilbun.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/businesses/stores")
@PreAuthorize("hasRole('ENTREPRENEUR')")
@RequiredArgsConstructor
public class BusinessesStoreController {
  private final StoreService storeService;
  private final MeetingService meetingService;

  /**
   * 모임 주문 목록 조회
   */
  @GetMapping("/{storeId}/meetings")
  public ResponseEntity<Page<MeetingPurchaseResponse>> getAllMeetingPurchaseByStoreId(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
      @RequestParam(name = "status", required = false, defaultValue = "") String status,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size,
      @RequestParam(name = "menuPage", required = false, defaultValue = "0") int menuPage,
      @RequestParam(name = "menuSize", required = false, defaultValue = "10") int menuSize
  ) {
    return ResponseEntity.ok(storeService.getAllMeetingPurchaseByStoreId(
        entrepreneur.getId(), storeId, status, page, size, menuPage, menuSize
    ));
  }

  /**
   * 모임 주문 상세 조회
   */
  @GetMapping("/{storeId}/meetings/{meetingId}")
  public ResponseEntity<Page<PurchaseDto.MenuResponse>> getMeetingPurchaseByStoreIdAndMeetingId(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
      @PathVariable("meetingId") Long meetingId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(meetingService.getMeetingPurchaseByStoreIdAndMeetingId(
        entrepreneur.getId(), storeId, meetingId, page, size
    ));
  }
}
