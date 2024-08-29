package com.zerobase.babdeusilbun.controller.menu;

import static com.zerobase.babdeusilbun.swagger.annotation.menu.EntrepreneurMenuSwagger.DeleteMenuSwagger;
import static com.zerobase.babdeusilbun.swagger.annotation.menu.EntrepreneurMenuSwagger.UpdateMenuSwagger;
import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.zerobase.babdeusilbun.dto.MenuDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/businesses")
@PreAuthorize("hasRole('ENTREPRENEUR')")
@RequiredArgsConstructor
public class EntrepreneurMenuController {
    private final MenuService menuService;

    @PatchMapping(value = "/menus/{menuId}",
            consumes = {MULTIPART_FORM_DATA_VALUE, APPLICATION_JSON_VALUE})
    @UpdateMenuSwagger
    public ResponseEntity<Void> updateMenu(
            @AuthenticationPrincipal CustomUserDetails entrepreneur,
            @PathVariable("menuId") Long menuId,
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
    @DeleteMapping(value = "/menus/{menuId}")
    @DeleteMenuSwagger
    public ResponseEntity<Void> deleteMenu(
            @AuthenticationPrincipal CustomUserDetails entrepreneur,
            @PathVariable("menuId") Long menuId) {

        menuService.deleteMenu(entrepreneur.getId(), menuId);
        return ResponseEntity.ok().build();
    }
}
