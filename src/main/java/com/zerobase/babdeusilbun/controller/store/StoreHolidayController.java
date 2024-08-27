package com.zerobase.babdeusilbun.controller.store;


import static com.zerobase.babdeusilbun.dto.HolidayDto.*;
import static com.zerobase.babdeusilbun.swagger.annotation.store.StoreHolidaySwagger.*;
import static org.springframework.http.HttpStatus.NOT_MODIFIED;
import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;

import com.zerobase.babdeusilbun.dto.HolidayDto;
import com.zerobase.babdeusilbun.dto.HolidayDto.Information;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.StoreService;
import com.zerobase.babdeusilbun.swagger.annotation.store.StoreHolidaySwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class StoreHolidayController {

  private final StoreService storeService;

  /**
   * 상점별 휴무일 조회
   */
  @GetMapping("/stores/{storeId}/holidays")
  @GetAllHolidaysSwagger
  public ResponseEntity<Page<Information>> getAllHolidays(
      @PathVariable Long storeId,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(storeService.getAllHolidays(storeId, page, size));
  }

  /**
   * 상점 휴무일 등록
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @PostMapping("/businesses/stores/{storeId}/holidays")
  @EnrollSchoolsToStoreSwagger
  public ResponseEntity<Void> enrollSchoolsToStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable Long storeId,
      @RequestBody HolidaysRequest request
  ) {
    int successCount = storeService.enrollHolidaysToStore(entrepreneur.getId(), storeId, request);

    if (request.getHolidays().isEmpty() || successCount == 0) {
      return ResponseEntity.status(NOT_MODIFIED).build();
    }

    return (successCount != request.getHolidays().size()) ?
        ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
  }

  /**
   * 상점 휴무일 삭제
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @DeleteMapping("/businesses/stores/{storeId}/holidays")
  @DeleteSchoolsOnStoreSwagger
  public ResponseEntity<Void> deleteSchoolsOnStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable Long storeId,
      @RequestBody HolidaysRequest request
  ) {
    int successCount = storeService.deleteHolidaysOnStore(entrepreneur.getId(), storeId, request);

    if (request.getHolidays().isEmpty() || successCount == 0) {
      return ResponseEntity.status(NOT_MODIFIED).build();
    }

    return (successCount != request.getHolidays().size()) ?
        ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
  }

}
