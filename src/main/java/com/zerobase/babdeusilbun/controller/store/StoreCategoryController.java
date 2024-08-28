package com.zerobase.babdeusilbun.controller.store;

import static com.zerobase.babdeusilbun.dto.CategoryDto.*;
import static com.zerobase.babdeusilbun.swagger.annotation.store.StoreCategorySwagger.*;
import static org.springframework.http.HttpStatus.NOT_MODIFIED;
import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;

import com.zerobase.babdeusilbun.dto.CategoryDto;
import com.zerobase.babdeusilbun.dto.CategoryDto.Information;
import com.zerobase.babdeusilbun.dto.StoreCategoryDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.StoreService;
import com.zerobase.babdeusilbun.swagger.annotation.store.StoreCategorySwagger;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreCategoryController {

  private final StoreService storeService;

  /**
   * 카테고리 조회
   */
  @GetMapping("/stores/categories")
  @GetAllCategoriesSwagger
  public ResponseEntity<Page<Information>> getAllCategories(
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(storeService.getAllCategories(page, size));
  }


  /**
   * 상점에 카테고리 등록
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @PostMapping("/businesses/stores/{storeId}/categories")
  @EnrollToCategorySwagger
  public ResponseEntity<Void> enrollToCategory(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable Long storeId,
      @RequestBody IdsRequest request
  ) {
    int successCount = storeService.enrollToCategory(entrepreneur.getId(), storeId, request);

    if (request.getCategoryIds().isEmpty() || successCount == 0) {
      return ResponseEntity.status(NOT_MODIFIED).build();
    }

    return (successCount != request.getCategoryIds().size()) ?
        ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
  }

  /**
   * 상점에 카테고리 삭제
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @DeleteMapping("/businesses/stores/{storeId}/categories")
  @DeleteOnCategorySwagger
  public ResponseEntity<Void> deleteOnCategory(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable Long storeId,
      @RequestBody IdsRequest request
  ) {
    int successCount = storeService.deleteOnCategory(entrepreneur.getId(), storeId, request);

    if (request.getCategoryIds().isEmpty() || successCount == 0) {
      return ResponseEntity.status(NOT_MODIFIED).build();
    }

    return (successCount != request.getCategoryIds().size()) ?
        ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
  }

  /**
   * 상점별 카테고리 조회
   */
  @GetMapping("/stores/{storeId}/categories")
  @GetAllCategoriesByStoreSwagger
  public ResponseEntity<Page<StoreCategoryDto.Information>> getAllCategoriesByStore(
      @PathVariable Long storeId,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(storeService.getAllCategories(storeId, page, size));
  }

}
