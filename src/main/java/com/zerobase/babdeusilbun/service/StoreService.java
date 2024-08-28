package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.CategoryDto;
import com.zerobase.babdeusilbun.dto.EntrepreneurDto;
import com.zerobase.babdeusilbun.dto.HolidayDto;
import com.zerobase.babdeusilbun.dto.MenuDto;
import com.zerobase.babdeusilbun.dto.PurchaseDto;
import com.zerobase.babdeusilbun.dto.SchoolDto;
import com.zerobase.babdeusilbun.dto.StoreCategoryDto;
import com.zerobase.babdeusilbun.dto.StoreDto;
import com.zerobase.babdeusilbun.dto.StoreDto.CreateRequest;
import com.zerobase.babdeusilbun.dto.StoreImageDto;
import com.zerobase.babdeusilbun.dto.StoreSchoolDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface StoreService {
  StoreDto.IdResponse createStore(Long entrepreneurId, CreateRequest request);
  int uploadImageToStore(Long entrepreneurId, List<MultipartFile> images, Long storeId);
  Page<CategoryDto.Information> getAllCategories(int page, int size);
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
      (Long userId, List<Long> categoryList, String searchMenu, Long schoolId, String sortCriteria, Pageable pageable);
  void deleteStore(Long entrepreneurId, Long storeId);
  Page<StoreDto.SimpleInformation> getAllStoresByEntrepreneur(
      Long entrepreneurId, int page, int size, boolean unprocessedOnly);
  StoreDto.PrincipalInformation getStore(Long storeId);
  Page<HolidayDto.Information> getAllHolidays(Long storeId, int page, int size);
  Page<StoreCategoryDto.Information> getAllCategories(Long storeId, int page, int size);
  Page<MenuDto.Information> getAllMenus(Long storeId, int page, int size);
  EntrepreneurDto.SimpleInformation getEntrepreneur(Long storeId);
  Page<StoreSchoolDto.Information> getAllSchools(Long storeId, int page, int size);
  Page<StoreImageDto.Information> getAllImages(Long storeId, int page, int size);
  StoreImageDto.Thumbnail getThumbnail(Long storeId);
  Page<PurchaseDto.MeetingPurchaseResponse> getAllMeetingPurchaseByStoreId(
      Long entrepreneurId, Long storeId, String status, int page, int size, int menuPage, int menuSize);
}
