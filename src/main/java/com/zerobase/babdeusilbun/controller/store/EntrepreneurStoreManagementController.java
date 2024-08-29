package com.zerobase.babdeusilbun.controller.store;

import static com.zerobase.babdeusilbun.dto.StoreDto.IdResponse;
import static com.zerobase.babdeusilbun.dto.StoreDto.UpdateRequest;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_MODIFIED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.zerobase.babdeusilbun.dto.CategoryDto.IdsRequest;
import com.zerobase.babdeusilbun.dto.HolidayDto.HolidaysRequest;
import com.zerobase.babdeusilbun.dto.MenuDto;
import com.zerobase.babdeusilbun.dto.SchoolDto;
import com.zerobase.babdeusilbun.dto.StoreDto.CreateRequest;
import com.zerobase.babdeusilbun.dto.StoreImageDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.MenuService;
import com.zerobase.babdeusilbun.service.StoreService;
import com.zerobase.babdeusilbun.swagger.annotation.store.EntrepreneurStoreManagementSwagger.CreateMenuSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.store.EntrepreneurStoreManagementSwagger.CreateStoreSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.store.EntrepreneurStoreManagementSwagger.DeleteHolidaysOnStoreSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.store.EntrepreneurStoreManagementSwagger.DeleteImageOnStoreSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.store.EntrepreneurStoreManagementSwagger.DeleteOnCategorySwagger;
import com.zerobase.babdeusilbun.swagger.annotation.store.EntrepreneurStoreManagementSwagger.DeleteSchoolsOnStoreSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.store.EntrepreneurStoreManagementSwagger.DeleteStoreSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.store.EntrepreneurStoreManagementSwagger.EnrollHolidaysToStoreSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.store.EntrepreneurStoreManagementSwagger.EnrollImagesToStoreSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.store.EntrepreneurStoreManagementSwagger.EnrollSchoolsToStoreSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.store.EntrepreneurStoreManagementSwagger.EnrollToCategorySwagger;
import com.zerobase.babdeusilbun.swagger.annotation.store.EntrepreneurStoreManagementSwagger.UpdateStoreImageInformationSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.store.EntrepreneurStoreManagementSwagger.UpdateStoreInformationSwagger;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/businesses/stores")
@PreAuthorize("hasRole('ENTREPRENEUR')")
@RequiredArgsConstructor
public class EntrepreneurStoreManagementController {
  private final StoreService storeService;
  private final MenuService menuService;

  /**
   * 상점 등록
   */
  @PostMapping(consumes = {MULTIPART_FORM_DATA_VALUE, APPLICATION_JSON_VALUE})
  @CreateStoreSwagger
  public ResponseEntity<IdResponse> createStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @RequestPart(value = "files", required = false) List<MultipartFile> images,
      @RequestPart(value = "request") CreateRequest request) {
    IdResponse storeId = storeService.createStore(entrepreneur.getId(), request);

    int uploadSuccessImageCount = storeService.uploadImageToStore(entrepreneur.getId(), images, storeId.getStoreId());

    if (images != null && images.size() != uploadSuccessImageCount) {
      return ResponseEntity.status(PARTIAL_CONTENT).body(storeId);
    }

    return ResponseEntity.status(CREATED).body(storeId);
  }

  /**
   * 메뉴 등록
   */
  @PostMapping(value = "/{storeId}/menus",
      consumes = {MULTIPART_FORM_DATA_VALUE, APPLICATION_JSON_VALUE})
  @CreateMenuSwagger
  public ResponseEntity<Void> createMenu(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
      @RequestPart(value = "file", required = false) MultipartFile image,
      @RequestPart(value = "request") MenuDto.CreateRequest request) {

    MenuDto.CreateRequest result = menuService.createMenu(entrepreneur.getId(), storeId, image, request);

    return (image != null && result.getImage() == null) ?
        ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.status(CREATED).build();
  }

  /**
   * 상점에 카테고리 등록
   */
  @PostMapping("/{storeId}/categories")
  @EnrollToCategorySwagger
  public ResponseEntity<Void> enrollToCategory(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
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
  @DeleteMapping("/{storeId}/categories")
  @DeleteOnCategorySwagger
  public ResponseEntity<Void> deleteOnCategory(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
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
   * 상점에 배달가능 캠퍼스 등록
   */
  @PostMapping("/{storeId}/schools")
  @EnrollSchoolsToStoreSwagger
  public ResponseEntity<Void> enrollSchoolsToStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
      @RequestBody SchoolDto.IdsRequest request
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
  @DeleteMapping("/{storeId}/schools")
  @DeleteSchoolsOnStoreSwagger
  public ResponseEntity<Void> deleteSchoolsOnStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
      @RequestBody SchoolDto.IdsRequest request
  ) {
    int successCount = storeService.deleteSchoolsOnStore(entrepreneur.getId(), storeId, request);

    if (request.getSchoolIds().isEmpty() || successCount == 0) {
      return ResponseEntity.status(NOT_MODIFIED).build();
    }

    return (successCount != request.getSchoolIds().size()) ?
        ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
  }

  /**
   * 상점 휴무일 등록
   */
  @PostMapping("/{storeId}/holidays")
  @EnrollHolidaysToStoreSwagger
  public ResponseEntity<Void> enrollHolidaysToStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
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
  @DeleteMapping("/{storeId}/holidays")
  @DeleteHolidaysOnStoreSwagger
  public ResponseEntity<Void> deleteHolidaysOnStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
      @RequestBody HolidaysRequest request
  ) {
    int successCount = storeService.deleteHolidaysOnStore(entrepreneur.getId(), storeId, request);

    if (request.getHolidays().isEmpty() || successCount == 0) {
      return ResponseEntity.status(NOT_MODIFIED).build();
    }

    return (successCount != request.getHolidays().size()) ?
        ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
  }

  /**
   * 상점 정보 수정
   */
  @PatchMapping("/{storeId}")
  @UpdateStoreInformationSwagger
  public ResponseEntity<Void> updateStoreInformation(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
      @RequestBody UpdateRequest request
  ) {
    storeService.updateStoreInformation(entrepreneur.getId(), storeId, request);

    return ResponseEntity.ok().build();
  }

  /**
   * 상점 이미지 등록
   */
  @PostMapping(value = "/{storeId}/images", consumes = {MULTIPART_FORM_DATA_VALUE})
  @EnrollImagesToStoreSwagger
  public ResponseEntity<Void> enrollImagesToStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
      @RequestPart(value = "files", required = false) List<MultipartFile> images
  ) {
    int successCount = storeService.uploadImageToStore(entrepreneur.getId(), images, storeId);

    if (images == null || images.isEmpty() || successCount == 0) {
      return ResponseEntity.status(NOT_MODIFIED).build();
    }

    return (successCount != images.size()) ?
        ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
  }

  /**
   * 상점 이미지 삭제
   */
  @DeleteMapping("/{storeId}/images/{imageId}")
  @DeleteImageOnStoreSwagger
  public ResponseEntity<Void> deleteImageOnStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
      @PathVariable("imageId") Long imageId
  ) {
    if (storeService.deleteImageOnStore(entrepreneur.getId(), storeId, imageId)) {
      return ResponseEntity.status(NO_CONTENT).build();
    }

    return ResponseEntity.status(PARTIAL_CONTENT).build();
  }

  /**
   * 상점 이미지 설정변경
   */
  @PatchMapping("/{storeId}/images/{imageId}")
  @UpdateStoreImageInformationSwagger
  public ResponseEntity<Void> updateStoreImageInformation(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
      @PathVariable("imageId") Long imageId,
      @RequestBody StoreImageDto.UpdateRequest request
  ) {
    storeService.updateStoreImage(entrepreneur.getId(), storeId, imageId, request);

    return ResponseEntity.ok().build();
  }

  /**
   * 상점 삭제
   */
  @DeleteMapping("/{storeId}")
  @DeleteStoreSwagger
  public ResponseEntity<Void> deleteStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId
  ) {
    storeService.deleteStore(entrepreneur.getId(), storeId);

    return ResponseEntity.status(NO_CONTENT).build();
  }
}
