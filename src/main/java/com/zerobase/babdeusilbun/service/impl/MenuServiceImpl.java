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

        Store store = storeRepository.findByIdAndEntrepreneur(storeId, entrepreneur)
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

    // 이미지 업로드
    public void createImage(MultipartFile image, MenuDto.CreateRequest request) {
        List<String> uploadUrlList = imageComponent.uploadImageList(List.of(image), MENU_IMAGE_FOLDER);

        if (uploadUrlList.isEmpty()) {
            request.setImage(null);
            return;
        }

        request.setImage(uploadUrlList.getFirst());
    }
}
