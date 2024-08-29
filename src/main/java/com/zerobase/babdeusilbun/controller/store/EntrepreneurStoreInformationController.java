package com.zerobase.babdeusilbun.controller.store;

import com.zerobase.babdeusilbun.dto.PurchaseDto;
import com.zerobase.babdeusilbun.dto.PurchaseDto.MeetingPurchaseResponse;
import com.zerobase.babdeusilbun.dto.StoreDto.SimpleInformation;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.MeetingService;
import com.zerobase.babdeusilbun.service.StoreService;
import com.zerobase.babdeusilbun.swagger.annotation.store.EntrepreneurStoreInformationSwagger.GetAllMeetingPurchaseByStoreIdSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.store.EntrepreneurStoreInformationSwagger.GetAllStoresByEntrepreneurSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.store.EntrepreneurStoreInformationSwagger.GetMeetingPurchaseByStoreIdAndMeetingId;
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
public class EntrepreneurStoreInformationController {
  private final StoreService storeService;
  private final MeetingService meetingService;

  /**
   * 등록한 상점 리스트 조회
   */
  @GetMapping
  @GetAllStoresByEntrepreneurSwagger
  public ResponseEntity<Page<SimpleInformation>> getAllStoresByEntrepreneur(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int size,
      @RequestParam(required = false, defaultValue = "false") boolean unprocessedOnly) {
    return ResponseEntity.ok(
        storeService.getAllStoresByEntrepreneur(entrepreneur.getId(), page, size, unprocessedOnly));
  }

  /**
   * 모임 주문 목록 조회
   */
  @GetMapping("/{storeId}/meetings")
  @GetAllMeetingPurchaseByStoreIdSwagger
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
  @GetMeetingPurchaseByStoreIdAndMeetingId
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
