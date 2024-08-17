package com.zerobase.babdeusilbun.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;

import com.zerobase.babdeusilbun.dto.CategoryDto;
import com.zerobase.babdeusilbun.dto.SchoolDto;
import com.zerobase.babdeusilbun.dto.StoreDto;
import com.zerobase.babdeusilbun.dto.StoreDto.CreateRequest;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.StoreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreController {
  private final StoreService storeService;

  /**
   * 상점 등록
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @PostMapping(value = "/businesses/stores",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<StoreDto.IdResponse> createStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @RequestPart(value = "files", required = false) List<MultipartFile> images,
      @RequestPart(value = "request") CreateRequest request) {
    StoreDto.IdResponse storeId = storeService.createStore(entrepreneur.getId(), request);

    int uploadSuccessImageCount = storeService.uploadImageToStore(entrepreneur.getId(), images, storeId.getStoreId());

    if (images != null && images.size() != uploadSuccessImageCount) {
      return ResponseEntity.status(PARTIAL_CONTENT).body(storeId);
    }

    return ResponseEntity.status(CREATED).body(storeId);
  }

  /**
   * 카테고리 조회
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @GetMapping("/businesses/stores/categories")
  public ResponseEntity<Page<CategoryDto.Information>> getAllCategories(
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(storeService.getAllCategories(page, size));
  }


  /**
   * 상점에 카테고리 등록
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @PostMapping("/businesses/stores/{storeId}/categories")
  public ResponseEntity<Void> enrollToCategory(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
      @RequestBody CategoryDto.IdsRequest request
  ) {
    int successCount = storeService.enrollToCategory(entrepreneur.getId(), storeId, request);

    if (request.getCategoryIds().isEmpty() || successCount == 0) {
      return ResponseEntity.status(NO_CONTENT).build();
    }

    return (successCount != request.getCategoryIds().size()) ?
        ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
  }

  /**
   * 상점에 카테고리 삭제
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @DeleteMapping("/businesses/stores/{storeId}/categories")
  public ResponseEntity<Void> deleteOnCategory(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
      @RequestBody CategoryDto.IdsRequest request
  ) {
    int successCount = storeService.deleteOnCategory(entrepreneur.getId(), storeId, request);

    if (request.getCategoryIds().isEmpty() || successCount == 0) {
      return ResponseEntity.status(NO_CONTENT).build();
    }

    return (successCount != request.getCategoryIds().size()) ?
        ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
  }

  /**
   * 상점에 배달가능 캠퍼스 등록
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @PostMapping("/businesses/stores/{storeId}/schools")
  public ResponseEntity<Void> enrollSchoolsToStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
      @RequestBody SchoolDto.IdsRequest request
  ) {
    int successCount = storeService.enrollSchoolsToStore(entrepreneur.getId(), storeId, request);

    if (request.getSchoolIds().isEmpty() || successCount == 0) {
      return ResponseEntity.status(NO_CONTENT).build();
    }

    return (successCount != request.getSchoolIds().size()) ?
        ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
  }

  /**
   * 상점에 배달가능 캠퍼스 삭제
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @DeleteMapping("/businesses/stores/{storeId}/schools")
  public ResponseEntity<Void> deleteSchoolsOnStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
      @RequestBody SchoolDto.IdsRequest request
  ) {
    int successCount = storeService.deleteSchoolsOnStore(entrepreneur.getId(), storeId, request);

    if (request.getSchoolIds().isEmpty() || successCount == 0) {
      return ResponseEntity.status(NO_CONTENT).build();
    }

    return (successCount != request.getSchoolIds().size()) ?
        ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
  }
}
