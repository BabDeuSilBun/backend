package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.CategoryDto;
import com.zerobase.babdeusilbun.dto.CategoryDto.Information;
import com.zerobase.babdeusilbun.dto.HolidayDto;
import com.zerobase.babdeusilbun.dto.SchoolDto;
import com.zerobase.babdeusilbun.dto.StoreDto;
import com.zerobase.babdeusilbun.dto.StoreDto.CreateRequest;
import com.zerobase.babdeusilbun.dto.StoreImageDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface StoreService {
  StoreDto.IdResponse createStore(Long entrepreneurId, CreateRequest request);
  int uploadImageToStore(Long entrepreneurId, List<MultipartFile> images, Long storeId);
  Page<Information> getAllCategories(int page, int size);
  int enrollToCategory(Long entrepreneurId, Long storeId, CategoryDto.IdsRequest request);
  int deleteOnCategory(Long entrepreneurId, Long storeId, CategoryDto.IdsRequest request);
  int enrollSchoolsToStore(Long entrepreneurId, Long storeId, SchoolDto.IdsRequest request);
  int deleteSchoolsOnStore(Long entrepreneurId, Long storeId, SchoolDto.IdsRequest request);
  int enrollHolidaysToStore(Long entrepreneurId, Long storeId, HolidayDto.HolidaysRequest request);
  int deleteHolidaysOnStore(Long entrepreneurId, Long storeId, HolidayDto.HolidaysRequest request);
  boolean deleteImageOnStore(Long entrepreneurId, Long storeId, Long imageId);
  void updateStoreImage(Long entrepreneurId, Long storeId, Long imageId, StoreImageDto.UpdateRequest request);
  void updateStoreInformation(Long entrepreneurId, Long storeId, StoreDto.UpdateRequest request);

  Page<StoreDto.Information> getAvailStoreList
      (List<Long> categoryList, String searchMenu, Long schoolId, String sortCriteria, Pageable pageable);
}
