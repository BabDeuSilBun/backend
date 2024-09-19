package com.zerobase.babdeusilbun.service.impl;

import com.zerobase.babdeusilbun.component.ImageComponent;
import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.Menu;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.dto.MenuDto;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.repository.EntrepreneurRepository;
import com.zerobase.babdeusilbun.repository.MenuRepository;
import com.zerobase.babdeusilbun.repository.StoreRepository;
import com.zerobase.babdeusilbun.service.MenuService;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.zerobase.babdeusilbun.exception.ErrorCode.*;
import static com.zerobase.babdeusilbun.util.ImageUtility.MENU_IMAGE_FOLDER;

@Slf4j
@Service
@AllArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final EntrepreneurRepository entrepreneurRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    private final ImageComponent imageComponent;

    // 메뉴 등록
    @Override
    public MenuDto.CreateRequest createMenu(Long entrepreneurId, Long storeId, MultipartFile image, MenuDto.CreateRequest request) {
        Entrepreneur entrepreneur = entrepreneurRepository
                .findByIdAndDeletedAtIsNull(entrepreneurId).orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND));

        Store store = storeRepository.findByIdAndEntrepreneurAndDeletedAtIsNull(storeId, entrepreneur)
                .orElseThrow(() -> new CustomException(NO_AUTH_ON_STORE));

        if (menuRepository.existsByStoreAndNameAndPriceAndDeletedAtIsNull(store, request.getName(), request.getPrice())) {
            throw new CustomException(ALREADY_EXIST_MENU);
        }

        if(image != null) {
            createImage(image, request);
        } else {
            request.setImage(null);
        }

        Menu menu = menuRepository.save(request.toEntity(store));

        return new MenuDto.CreateRequest(menu.getName(), menu.getDescription(), menu.getImage(), menu.getPrice());
    }

    // 메뉴 수정
    @Override
    @Transactional
    public MenuDto.UpdateRequest updateMenu(Long entrepreneurId, Long menuId, MultipartFile image, MenuDto.UpdateRequest request) {
        // menuId를 기반으로 메뉴 불러오기
        Menu menu = menuRepository.findByIdAndDeletedAtIsNull(menuId)
                .orElseThrow(() -> new CustomException(MENU_NOT_FOUND));

        // 해당 메뉴를 소지한 상점 주인이 현재 로그인한 사업자와 일치하는지 확인
        if(entrepreneurId != menu.getStore().getEntrepreneur().getId()) {
            throw new CustomException(NO_AUTH_ON_MENU);
        }

        // 현재 메뉴가 등록된 상점에서 수정한 메뉴랑 동일한 이름,가격을 가진 메뉴가 있는지 확인
        if (menuRepository.existsByStoreAndNameAndPriceAndDeletedAtIsNull(menu.getStore(), request.getName(), request.getPrice())) {
            throw new CustomException(ALREADY_EXIST_MENU);
        }

        if(image != null) {
            updateImage(menu, image, request);
        } else if (request.getImage() != null && request.getImage().isEmpty() && StringUtils.isNotBlank(menu.getImage())) {
            imageComponent.deleteImageByUrl(menu.getImage());
        }

        menu.update(request);
        return request;
    }

    // 메뉴 삭제
    @Override
    @Transactional
    public Menu deleteMenu(Long entrepreneurId, Long menuId) {
        // menuId를 기반으로 메뉴 불러오기
        Menu menu = menuRepository.findByIdAndDeletedAtIsNull(menuId)
                .orElseThrow(() -> new CustomException(MENU_NOT_FOUND));

        // 해당 메뉴를 소지한 상점 주인이 현재 로그인한 사업자와 일치하는지 확인
        if(entrepreneurId != menu.getStore().getEntrepreneur().getId()) {
            throw new CustomException(NO_AUTH_ON_MENU);
        }

        menu.delete();

        return menu;
    }

    // 이미지 처음 업로드
    private void createImage(MultipartFile image, MenuDto.CreateRequest request) {
        List<String> uploadUrlList = imageComponent.uploadImageList(List.of(image), MENU_IMAGE_FOLDER);

        if (uploadUrlList.isEmpty()) {
            request.setImage(null);
            return;
        }

        request.setImage(uploadUrlList.getFirst());
    }

    // 이미지 수정
    private void updateImage(Menu menu, MultipartFile image, MenuDto.UpdateRequest request) {
        List<String> uploadUrlList = imageComponent.uploadImageList(List.of(image), MENU_IMAGE_FOLDER);

        if (uploadUrlList.isEmpty()) {
            request.setImage(null);
            return;
        }

        if (StringUtils.isNotBlank(menu.getImage())) {
            imageComponent.deleteImageByUrl(menu.getImage());
        }

        request.setImage(uploadUrlList.getFirst());
    }
}
