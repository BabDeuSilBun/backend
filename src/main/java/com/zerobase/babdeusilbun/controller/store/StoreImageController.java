package com.zerobase.babdeusilbun.controller.store;

import static com.zerobase.babdeusilbun.dto.StoreImageDto.*;
import static com.zerobase.babdeusilbun.swagger.annotation.store.StoreImageSwagger.*;
import static org.springframework.http.HttpStatus.NOT_MODIFIED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;
import static org.springframework.http.MediaType.*;

import com.zerobase.babdeusilbun.dto.StoreImageDto.Information;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.StoreService;
import com.zerobase.babdeusilbun.swagger.annotation.store.StoreImageSwagger;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
@RequestMapping("/businesses/stores/{storeId}/images")
@RequiredArgsConstructor
public class StoreImageController {

  private final StoreService storeService;

  /**
   * 상점 이미지 전체 조회
   */
  @GetMapping
  @GetAllImagesSwagger
  public ResponseEntity<Page<Information>> getAllImages(
      @PathVariable Long storeId,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(storeService.getAllImages(storeId, page, size));
  }

  /**
   * 상점 이미지 등록
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @PostMapping(consumes = {MULTIPART_FORM_DATA_VALUE})
  @EnrollImagesToStoreSwagger
  public ResponseEntity<Void> enrollImagesToStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable Long storeId,
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
  @DeleteMapping("/{imageId}")
  @DeleteImageOnStoreSwagger
  public ResponseEntity<Void> deleteImageOnStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable Long storeId,
      @PathVariable Long imageId
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
  @PatchMapping("/{imageId}")
  @UpdateStoreImageInformationSwagger
  public ResponseEntity<Void> updateStoreImageInformation(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable Long storeId,
      @PathVariable Long imageId,
      @RequestBody UpdateRequest request
  ) {
    storeService.updateStoreImage(entrepreneur.getId(), storeId, imageId, request);

    return ResponseEntity.ok().build();
  }

}
