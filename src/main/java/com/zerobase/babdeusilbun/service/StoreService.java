package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.CategoryDto;
import com.zerobase.babdeusilbun.dto.CategoryDto.Information;
import com.zerobase.babdeusilbun.dto.StoreDto.CreateRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface StoreService {
  int createStore(Long entrepreneurId, List<MultipartFile> images, CreateRequest request);
  Page<Information> getAllCategories(int page, int size);
  int enrollToCategory(Long entrepreneurId, Long storeId, CategoryDto.IdsRequest request);
  int deleteOnCategory(Long entrepreneurId, Long storeId, CategoryDto.IdsRequest request);
}
