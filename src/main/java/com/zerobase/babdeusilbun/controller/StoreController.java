package com.zerobase.babdeusilbun.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_MODIFIED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;

import com.zerobase.babdeusilbun.dto.CategoryDto;
import com.zerobase.babdeusilbun.dto.EntrepreneurDto;
import com.zerobase.babdeusilbun.dto.HolidayDto;
import com.zerobase.babdeusilbun.dto.MenuDto;
import com.zerobase.babdeusilbun.dto.SchoolDto;
import com.zerobase.babdeusilbun.dto.StoreCategoryDto;
import com.zerobase.babdeusilbun.dto.StoreDto;
import com.zerobase.babdeusilbun.dto.StoreDto.CreateRequest;
import com.zerobase.babdeusilbun.dto.StoreImageDto;
import com.zerobase.babdeusilbun.dto.StoreSchoolDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.StoreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
  @GetMapping("/stores/categories")
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
  public ResponseEntity<Void> deleteOnCategory(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
      @RequestBody CategoryDto.IdsRequest request
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
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @PostMapping("/businesses/stores/{storeId}/schools")
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
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @DeleteMapping("/businesses/stores/{storeId}/schools")
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
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @PostMapping("/businesses/stores/{storeId}/holidays")
  public ResponseEntity<Void> enrollSchoolsToStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
      @RequestBody HolidayDto.HolidaysRequest request
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
  public ResponseEntity<Void> deleteSchoolsOnStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
      @RequestBody HolidayDto.HolidaysRequest request
  ) {
    int successCount = storeService.deleteHolidaysOnStore(entrepreneur.getId(), storeId, request);

    if (request.getHolidays().isEmpty() || successCount == 0) {
      return ResponseEntity.status(NOT_MODIFIED).build();
    }

    return (successCount != request.getHolidays().size()) ?
        ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
  }

  /**
   * 상점 이미지 등록
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @PostMapping(value = "/businesses/stores/{storeId}/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
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
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @DeleteMapping("/businesses/stores/{storeId}/images/{imageId}")
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
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @PatchMapping("/businesses/stores/{storeId}/images/{imageId}")
  public ResponseEntity<Void> updateStoreInformation(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
      @PathVariable("imageId") Long imageId,
      @RequestBody StoreImageDto.UpdateRequest request
  ) {
    storeService.updateStoreImage(entrepreneur.getId(), storeId, imageId, request);

    return ResponseEntity.ok().build();
  }

  /**
   * 상점 정보 수정
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @PatchMapping("/businesses/stores/{storeId}")
  public ResponseEntity<Void> updateStoreInformation(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId,
      @RequestBody StoreDto.UpdateRequest request
  ) {
    storeService.updateStoreInformation(entrepreneur.getId(), storeId, request);

    return ResponseEntity.ok().build();
  }

  /**
   * 주문 가능 가게 리스트 검색/조회
   */
  @GetMapping("/users/stores")
  public ResponseEntity<Page<StoreDto.Information>> getAvailStoreList(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestParam(name = "foodCategoryFilter", defaultValue = "") List<Long> categoryList,
      @RequestParam(name = "searchMenu", required = false) String searchMenu,
      @RequestParam(name = "schoolId", required = false) Long schoolId,
      @RequestParam(name = "sortCriteria", defaultValue = "DEADLINE") String sortCriteria,
      Pageable pageable
  ) {

    return ResponseEntity.ok(
        storeService.getAvailStoreList(user.getId(), categoryList, searchMenu, schoolId, sortCriteria, pageable)
    );
  }

  /**
   * 상점 삭제
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @DeleteMapping("/businesses/stores/{storeId}")
  public ResponseEntity<Void> deleteStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable("storeId") Long storeId
  ) {
    storeService.deleteStore(entrepreneur.getId(), storeId);

    return ResponseEntity.status(NO_CONTENT).build();
  }

  /**
   * 등록한 상점 리스트 조회
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @GetMapping("/businesses/stores")
  public ResponseEntity<Page<StoreDto.SimpleInformation>> getAllStoresByEntrepreneur(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size,
      @RequestParam(name = "unprocessedOnly", required = false, defaultValue = "false") boolean unprocessedOnly) {
    return ResponseEntity.ok(
        storeService.getAllStoresByEntrepreneur(entrepreneur.getId(), page, size, unprocessedOnly));
  }

  /**
   * 상점 정보 조회
   */
  @GetMapping("/stores/{storeId}")
  public ResponseEntity<StoreDto.PrincipalInformation> getStoreInfo(
      @PathVariable("storeId") Long storeId) {
    return ResponseEntity.ok(storeService.getStore(storeId));
  }

  /**
   * 상점별 휴무일 조회
   */
  @GetMapping("/stores/{storeId}/holidays")
  public ResponseEntity<Page<HolidayDto.Information>> getAllHolidays(
      @PathVariable("storeId") Long storeId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(storeService.getAllHolidays(storeId, page, size));
  }

  /**
   * 상점별 카테고리 조회
   */
  @GetMapping("/stores/{storeId}/categories")
  public ResponseEntity<Page<StoreCategoryDto.Information>> getAllCategories(
      @PathVariable("storeId") Long storeId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(storeService.getAllCategories(storeId, page, size));
  }

  /**
   * 가게별 메뉴 리스트 조회
   */
  @GetMapping("/stores/{storeId}/menus")
  public ResponseEntity<Page<MenuDto.Information>> getAllMenus(
      @PathVariable("storeId") Long storeId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(storeService.getAllMenus(storeId, page, size));
  }

  /**
   * 상점별 사업자 정보 조회
   */
  @GetMapping("/stores/{storeId}/entrepreneur")
  public ResponseEntity<EntrepreneurDto.SimpleInformation> getEntrepreneur(
      @PathVariable("storeId") Long storeId) {
    return ResponseEntity.ok(storeService.getEntrepreneur(storeId));
  }

  /**
   * 상점별 배달가능 캠퍼스 조회
   */
  @GetMapping("/stores/{storeId}/schools")
  public ResponseEntity<Page<StoreSchoolDto.Information>> getAllSchools(
      @PathVariable("storeId") Long storeId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(storeService.getAllSchools(storeId, page, size));
  }

  /**
   * 상점 이미지 전체 조회
   */
  @GetMapping("/stores/{storeId}/images")
  public ResponseEntity<Page<StoreImageDto.Information>> getAllImages(
      @PathVariable("storeId") Long storeId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(storeService.getAllImages(storeId, page, size));
  }

  /**
   * 썸네일 조회
   */
  @GetMapping("/stores/{storeId}/thumbnail")
  public ResponseEntity<StoreImageDto.Thumbnail> getThumbnail(
      @PathVariable("storeId") Long storeId) {
    return ResponseEntity.ok(storeService.getThumbnail(storeId));
  }
}
