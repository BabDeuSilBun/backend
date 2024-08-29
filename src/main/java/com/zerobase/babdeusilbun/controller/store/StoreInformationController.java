package com.zerobase.babdeusilbun.controller.store;

import static com.zerobase.babdeusilbun.swagger.annotation.store.StoreInformationSwagger.GetAllMenuSwagger;
import static com.zerobase.babdeusilbun.swagger.annotation.store.StoreInformationSwagger.GetEntrepreneurSwagger;
import static com.zerobase.babdeusilbun.swagger.annotation.store.StoreInformationSwagger.GetStoreInfoSwagger;
import static com.zerobase.babdeusilbun.swagger.annotation.store.StoreInformationSwagger.GetThumbnailSwagger;

import com.zerobase.babdeusilbun.dto.EntrepreneurDto;
import com.zerobase.babdeusilbun.dto.HolidayDto;
import com.zerobase.babdeusilbun.dto.MenuDto.Information;
import com.zerobase.babdeusilbun.dto.StoreCategoryDto;
import com.zerobase.babdeusilbun.dto.StoreDto.PrincipalInformation;
import com.zerobase.babdeusilbun.dto.StoreImageDto;
import com.zerobase.babdeusilbun.dto.StoreSchoolDto;
import com.zerobase.babdeusilbun.service.StoreService;
import com.zerobase.babdeusilbun.swagger.annotation.store.StoreInformationSwagger.GetAllCategoriesByStoreSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.store.StoreInformationSwagger.GetAllHolidaysSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.store.StoreInformationSwagger.GetAllImagesSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.store.StoreInformationSwagger.GetAllSchoolsSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreInformationController {
  private final StoreService storeService;

  /**
   * 상점 정보 조회
   */
  @GetMapping("/{storeId}")
  @GetStoreInfoSwagger
  public ResponseEntity<PrincipalInformation> getStoreInfo(
      @PathVariable("storeId") Long storeId) {
    return ResponseEntity.ok(storeService.getStore(storeId));
  }

  /**
   * 상점별 휴무일 조회
   */
  @GetMapping("/{storeId}/holidays")
  @GetAllHolidaysSwagger
  public ResponseEntity<Page<HolidayDto.Information>> getAllHolidays(
      @PathVariable("storeId") Long storeId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(storeService.getAllHolidays(storeId, page, size));
  }

  /**
   * 가게별 메뉴 리스트 조회
   */
  @GetMapping("/{storeId}/menus")
  @GetAllMenuSwagger
  public ResponseEntity<Page<Information>> getAllMenus(
      @PathVariable("storeId") Long storeId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(storeService.getAllMenus(storeId, page, size));
  }

  /**
   * 상점별 사업자 정보 조회
   */
  @GetMapping("/{storeId}/entrepreneur")
  @GetEntrepreneurSwagger
  public ResponseEntity<EntrepreneurDto.SimpleInformation> getEntrepreneur(
      @PathVariable("storeId") Long storeId) {
    return ResponseEntity.ok(storeService.getEntrepreneur(storeId));
  }

  /**
   * 상점 이미지 전체 조회
   */
  @GetMapping("/{storeId}/images")
  @GetAllImagesSwagger
  public ResponseEntity<Page<StoreImageDto.Information>> getAllImages(
      @PathVariable("storeId") Long storeId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(storeService.getAllImages(storeId, page, size));
  }

  /**
   * 썸네일 조회
   */
  @GetMapping("/{storeId}/thumbnail")
  @GetThumbnailSwagger
  public ResponseEntity<StoreImageDto.Thumbnail> getThumbnail(
      @PathVariable("storeId") Long storeId) {
    return ResponseEntity.ok(storeService.getThumbnail(storeId));
  }

  /**
   * 상점별 카테고리 조회
   */
  @GetMapping("/{storeId}/categories")
  @GetAllCategoriesByStoreSwagger
  public ResponseEntity<Page<StoreCategoryDto.Information>> getAllCategoriesByStore(
      @PathVariable("storeId") Long storeId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(storeService.getAllCategories(storeId, page, size));
  }

  /**
   * 상점별 배달가능 캠퍼스 조회
   */
  @GetMapping("/{storeId}/schools")
  @GetAllSchoolsSwagger
  public ResponseEntity<Page<StoreSchoolDto.Information>> getAllSchools(
      @PathVariable("storeId") Long storeId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(storeService.getAllSchools(storeId, page, size));
  }
}
