package com.zerobase.babdeusilbun.controller;

import com.zerobase.babdeusilbun.dto.MenuDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /**
     * 메뉴 등록
     */
    @PreAuthorize("hasRole('ENTREPRENEUR')")
    @PostMapping(value = "/businesses/stores/{storeId}/menus",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> createMenu(
            @AuthenticationPrincipal
    CustomUserDetails entrepreneur,
            @PathVariable("storeId") Long storeId,
            @RequestPart(value = "file", required = false) MultipartFile image,
            @RequestPart(value = "request") MenuDto.CreateRequest request) {

        MenuDto.CreateRequest result = menuService.createMenu(entrepreneur.getId(), storeId, image, request);

        return (image != null && result.getImage() == null) ?
                ResponseEntity.status(PARTIAL_CONTENT).build() : ResponseEntity.status(CREATED).build();
    }

}
