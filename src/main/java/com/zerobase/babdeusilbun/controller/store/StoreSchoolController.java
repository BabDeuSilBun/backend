package com.zerobase.babdeusilbun.controller.store;

import static com.zerobase.babdeusilbun.dto.SchoolDto.*;
import static com.zerobase.babdeusilbun.swagger.annotation.store.StoreSchoolSwagger.*;
import static org.springframework.http.HttpStatus.NOT_MODIFIED;
import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;

import com.zerobase.babdeusilbun.dto.StoreSchoolDto.Information;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.StoreService;
import com.zerobase.babdeusilbun.swagger.annotation.store.StoreSchoolSwagger;
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
public class StoreSchoolController {

  private final StoreService storeService;

  /**
   * 상점별 배달가능 캠퍼스 조회
   */
  @GetMapping("/stores/{storeId}/schools")
  @GetAllSchoolsSwagger
  public ResponseEntity<Page<Information>> getAllSchools(
      @PathVariable Long storeId,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(storeService.getAllSchools(storeId, page, size));
  }

  /**
   * 상점에 배달가능 캠퍼스 등록
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @PostMapping("/businesses/stores/{storeId}/schools")
  @EnrollSchoolsToStoreSwagger
  public ResponseEntity<Void> enrollSchoolsToStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable Long storeId,
      @RequestBody IdsRequest request
  ) {
    int successCount = storeService.enrollSchoolsToStore(entrepreneur.getId(), storeId, request);

    if (request.getSchoolIds().isEmpty() || successCount == 0) {
      return ResponseEntity.status(NOT_MODIFIED).build();
    }

    return (successCount != request.getSchoolIds().size()) ?
        ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
  }

  /**
   * 상점에 배달가능 캠퍼스 삭제
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @DeleteMapping("/businesses/stores/{storeId}/schools")
  @DeleteSchoolsOnStoreSwagger
  public ResponseEntity<Void> deleteSchoolsOnStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable Long storeId,
      @RequestBody IdsRequest request
  ) {
    int successCount = storeService.deleteSchoolsOnStore(entrepreneur.getId(), storeId, request);

    if (request.getSchoolIds().isEmpty() || successCount == 0) {
      return ResponseEntity.status(NOT_MODIFIED).build();
    }

    return (successCount != request.getSchoolIds().size()) ?
        ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
  }

}
