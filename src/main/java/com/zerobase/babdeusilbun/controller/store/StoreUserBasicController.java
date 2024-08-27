package com.zerobase.babdeusilbun.controller.store;

import static com.zerobase.babdeusilbun.swagger.annotation.store.StoreUserBasicSwagger.*;

import com.zerobase.babdeusilbun.dto.StoreDto.Information;
import com.zerobase.babdeusilbun.service.StoreService;
import com.zerobase.babdeusilbun.swagger.annotation.store.StoreUserBasicSwagger;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/stores")
@RequiredArgsConstructor
public class StoreUserBasicController {

  private final StoreService storeService;

  /**
   * 주문 가능 가게 리스트 검색/조회
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping
  @GetAvailStoreListSwagger
  public ResponseEntity<Page<Information>> getAvailStoreList(
      @RequestParam List<Long> categoryList,
      @RequestParam String searchMenu,
      @RequestParam Long schoolId,
      @RequestParam String sortCriteria,
      Pageable pageable
  ) {

    return ResponseEntity.ok(
        storeService.getAvailStoreList(categoryList, searchMenu, schoolId, sortCriteria, pageable)
    );
  }


}
