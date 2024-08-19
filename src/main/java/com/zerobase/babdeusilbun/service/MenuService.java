package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.MenuDto;
import org.springframework.web.multipart.MultipartFile;

public interface MenuService {
    MenuDto.CreateRequest createMenu(Long entrepreneurId, Long storeId, MultipartFile image, MenuDto.CreateRequest request);
}
