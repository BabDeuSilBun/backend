package com.zerobase.babdeusilbun.controller;

import com.zerobase.babdeusilbun.dto.MenuDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.MenuService;
import com.zerobase.babdeusilbun.swagger.annotation.MajorSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.MenuSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.zerobase.babdeusilbun.swagger.annotation.MenuSwagger.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;
import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /**
     * 메뉴 등록
     */
    @PreAuthorize("hasRole('ENTREPRENEUR')")
    @PostMapping(value = "/stores/{storeId}/menus",
            consumes = {MULTIPART_FORM_DATA_VALUE, APPLICATION_JSON_VALUE})
    @CreateMenuSwagger
    public ResponseEntity<Void> createMenu(
            @AuthenticationPrincipal CustomUserDetails entrepreneur,
            @PathVariable Long storeId,
            @RequestPart(value = "file", required = false) MultipartFile image,
            @RequestPart(value = "request") MenuDto.CreateRequest request) {

        MenuDto.CreateRequest result = menuService.createMenu(entrepreneur.getId(), storeId, image, request);

        return (image != null && result.getImage() == null) ?
                ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.status(CREATED).build();
    }

    /**
     * 메뉴 수정
     */
    @PreAuthorize("hasRole('ENTREPRENEUR')")
    @PatchMapping(value = "/menus/{menuId}",
            consumes = {MULTIPART_FORM_DATA_VALUE, APPLICATION_JSON_VALUE})
    @UpdateMenuSwagger
    public ResponseEntity<Void> updateMenu(
            @AuthenticationPrincipal CustomUserDetails entrepreneur,
            @PathVariable Long menuId,
            @RequestPart(value = "file", required = false) MultipartFile image,
            @RequestPart(value = "request") MenuDto.UpdateRequest request
    ) {

        MenuDto.UpdateRequest result = menuService.updateMenu(entrepreneur.getId(), menuId, image, request);

        return (image != null && result.getImage() == null) ?
                ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.ok().build();
    }

    /**
     * 메뉴 삭제
     */
    @PreAuthorize("hasRole('ENTREPRENEUR')")
    @DeleteMapping(value = "/menus/{menuId}")
    @DeleteMenuSwagger
    public ResponseEntity<Void> deleteMenu(
            @AuthenticationPrincipal CustomUserDetails entrepreneur,
            @PathVariable Long menuId) {

        menuService.deleteMenu(entrepreneur.getId(), menuId);
        return ResponseEntity.ok().build();
    }
}
