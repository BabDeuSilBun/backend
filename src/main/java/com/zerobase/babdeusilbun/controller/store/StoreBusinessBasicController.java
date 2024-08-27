package com.zerobase.babdeusilbun.controller.store;

import static com.zerobase.babdeusilbun.dto.StoreDto.*;
import static com.zerobase.babdeusilbun.swagger.annotation.store.StoreBusinessBasicSwagger.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;
import static org.springframework.http.MediaType.*;

import com.zerobase.babdeusilbun.dto.StoreDto.CreateRequest;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.StoreService;
import com.zerobase.babdeusilbun.swagger.annotation.store.StoreBusinessBasicSwagger;
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
@RequestMapping("/api/businesses/stores")
@RequiredArgsConstructor
public class StoreBusinessBasicController {
  private final StoreService storeService;

  /**
   * 등록한 상점 리스트 조회
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
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
   * 상점 등록
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
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
   * 상점 정보 수정
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @PatchMapping("/{storeId}")
  @UpdateStoreInformationSwagger
  public ResponseEntity<Void> updateStoreInformation(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable Long storeId,
      @RequestBody UpdateRequest request
  ) {
    storeService.updateStoreInformation(entrepreneur.getId(), storeId, request);

    return ResponseEntity.ok().build();
  }

  /**
   * 상점 삭제
   */
  @PreAuthorize("hasRole('ENTREPRENEUR')")
  @DeleteMapping("/{storeId}")
  @DeleteStoreSwagger
  public ResponseEntity<Void> deleteStore(
      @AuthenticationPrincipal CustomUserDetails entrepreneur,
      @PathVariable Long storeId
  ) {
    storeService.deleteStore(entrepreneur.getId(), storeId);

    return ResponseEntity.status(NO_CONTENT).build();
  }



}
