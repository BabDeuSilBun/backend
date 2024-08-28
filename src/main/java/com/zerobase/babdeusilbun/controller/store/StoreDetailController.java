package com.zerobase.babdeusilbun.controller.store;

import static com.zerobase.babdeusilbun.swagger.annotation.store.StoreDetailSwagger.*;

import com.zerobase.babdeusilbun.dto.EntrepreneurDto;
import com.zerobase.babdeusilbun.dto.MenuDto.Information;
import com.zerobase.babdeusilbun.dto.StoreDto.PrincipalInformation;
import com.zerobase.babdeusilbun.dto.StoreImageDto;
import com.zerobase.babdeusilbun.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stores/{storeId}")
@RequiredArgsConstructor
public class StoreDetailController {

  private final StoreService storeService;

  /**
   * 상점 정보 조회
   */
  @GetMapping
  @GetStoreInfoSwagger
  public ResponseEntity<PrincipalInformation> getStoreInfo(
      @PathVariable Long storeId) {
    return ResponseEntity.ok(storeService.getStore(storeId));
  }

  /**
   * 가게별 메뉴 리스트 조회
   */
  @GetMapping("/menus")
  @GetAllMenuSwagger
  public ResponseEntity<Page<Information>> getAllMenus(
      @PathVariable Long storeId,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(storeService.getAllMenus(storeId, page, size));
  }

  /**
   * 상점별 사업자 정보 조회
   */
  @GetMapping("/entrepreneur")
  @GetEntrepreneurSwagger
  public ResponseEntity<EntrepreneurDto.SimpleInformation> getEntrepreneur(
      @PathVariable Long storeId) {
    return ResponseEntity.ok(storeService.getEntrepreneur(storeId));
  }

  /**
   * 썸네일 조회
   */
  @GetMapping("/thumbnail")
  @GetThumbnailSwagger
  public ResponseEntity<StoreImageDto.Thumbnail> getThumbnail(
      @PathVariable Long storeId) {
    return ResponseEntity.ok(storeService.getThumbnail(storeId));
  }

}
