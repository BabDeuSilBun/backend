package com.zerobase.babdeusilbun.controller.store;

import static com.zerobase.babdeusilbun.swagger.annotation.store.UserStoreInformationSwagger.GetAvailStoreListSwagger;

import com.zerobase.babdeusilbun.dto.StoreDto.Information;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.StoreService;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserStoreInformationController {
  private final StoreService storeService;

  /**
   * 주문 가능 가게 리스트 검색/조회
   */
  @GetMapping("/users/stores")
  @GetAvailStoreListSwagger
  public ResponseEntity<Page<Information>> getAvailStoreList(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestParam(value = "foodCategoryFilter", required = false) List<Long> categoryList,
      @RequestParam(value = "searchMenu", required = false) String searchMenu,
      @RequestParam(value = "schoolId", required = false) Long schoolId,
      @RequestParam("sortCriteria") String sortCriteria,
      @Parameter(description = "목록 페이지 번호와 한 페이지당 보이는 항목 개수 설정")
      Pageable pageable
  ) {

    return ResponseEntity.ok(
        storeService.getAvailStoreList(userDetails.getId(), categoryList, searchMenu, schoolId, sortCriteria, pageable)
    );
  }
}
