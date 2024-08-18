package com.zerobase.babdeusilbun.service.impl;

import static com.zerobase.babdeusilbun.exception.ErrorCode.ALREADY_EXIST_STORE;
import static com.zerobase.babdeusilbun.exception.ErrorCode.ENTREPRENEUR_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.NO_AUTH_ON_STORE;
import static com.zerobase.babdeusilbun.exception.ErrorCode.STORE_NOT_FOUND;
import static com.zerobase.babdeusilbun.util.ImageUtility.STORE_IMAGE_FOLDER;

import com.zerobase.babdeusilbun.component.ImageComponent;
import com.zerobase.babdeusilbun.domain.Category;
import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.Holiday;
import com.zerobase.babdeusilbun.domain.School;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.StoreCategory;
import com.zerobase.babdeusilbun.domain.StoreImage;
import com.zerobase.babdeusilbun.domain.StoreSchool;
import com.zerobase.babdeusilbun.dto.CategoryDto.IdsRequest;
import com.zerobase.babdeusilbun.dto.CategoryDto.Information;
import com.zerobase.babdeusilbun.dto.HolidayDto.HolidaysRequest;
import com.zerobase.babdeusilbun.dto.SchoolDto;
import com.zerobase.babdeusilbun.dto.StoreDto.CreateRequest;
import com.zerobase.babdeusilbun.dto.StoreDto.IdResponse;
import com.zerobase.babdeusilbun.dto.StoreDto.ImageUrl;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.repository.CategoryRepository;
import com.zerobase.babdeusilbun.repository.EntrepreneurRepository;
import com.zerobase.babdeusilbun.repository.HolidayRepository;
import com.zerobase.babdeusilbun.repository.SchoolRepository;
import com.zerobase.babdeusilbun.repository.StoreCategoryRepository;
import com.zerobase.babdeusilbun.repository.StoreImageRepository;
import com.zerobase.babdeusilbun.repository.StoreRepository;
import com.zerobase.babdeusilbun.repository.StoreSchoolRepository;
import com.zerobase.babdeusilbun.service.StoreService;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@AllArgsConstructor
public class StoreServiceImpl implements StoreService {
  private final EntrepreneurRepository entrepreneurRepository;
  private final StoreRepository storeRepository;
  private final StoreImageRepository imageRepository;
  private final SchoolRepository schoolRepository;
  private final StoreSchoolRepository storeSchoolRepository;
  private final CategoryRepository categoryRepository;
  private final StoreCategoryRepository storeCategoryRepository;
  private final HolidayRepository holidayRepository;
  private final ImageComponent imageComponent;

  @Override
  @Transactional
  public IdResponse createStore(Long entrepreneurId, CreateRequest request) {
    Entrepreneur entrepreneur = entrepreneurRepository
        .findByIdAndDeletedAtIsNull(entrepreneurId).orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND));

    if (storeRepository.existsByEntrepreneurAndNameAndAddressAndDeletedAtIsNull(
        entrepreneur, request.getName(), request.getAddress().toEntity())
    ) {
      throw new CustomException(ALREADY_EXIST_STORE);
    }

    Store store = storeRepository.save(request.toEntity(entrepreneur));

    return IdResponse.builder().storeId(store.getId()).build();
  }

  @Override
  @Transactional
  public int uploadImageToStore(Long entrepreneurId, List<MultipartFile> images, Long storeId) {
    Entrepreneur entrepreneur = entrepreneurRepository
        .findByIdAndDeletedAtIsNull(entrepreneurId).orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND));

    Store store = storeRepository
        .findByIdAndDeletedAtIsNull(storeId).orElseThrow(() -> new CustomException(STORE_NOT_FOUND));

    if (store.getEntrepreneur() != entrepreneur) {
      throw new CustomException(NO_AUTH_ON_STORE);
    }

    List<StoreImage> uploadImageList = new ArrayList<>();
    AtomicInteger sequence = new AtomicInteger(imageRepository.countByStore(store));

    imageComponent.uploadImageList(images, STORE_IMAGE_FOLDER).forEach(url ->
        uploadImageList.add(
            ImageUrl.builder()
                .url(url)
                .isRepresentative(sequence.get() == 0)
                .sequence(sequence.getAndIncrement())
                .build()
                .toEntity(store)
        )
    );

    if (!uploadImageList.isEmpty()) {
      imageRepository.saveAll(uploadImageList);
    }

    return uploadImageList.size();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Information> getAllCategories(int page, int size) {
    return categoryRepository.getAllCategories(page, size);
  }

  @Override
  @Transactional
  public int enrollToCategory(Long entrepreneurId, Long storeId, IdsRequest request) {
    Entrepreneur entrepreneur = entrepreneurRepository
        .findByIdAndDeletedAtIsNull(entrepreneurId).orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND));

    Store store = storeRepository
        .findByIdAndDeletedAtIsNull(storeId).orElseThrow(() -> new CustomException(STORE_NOT_FOUND));

    if (store.getEntrepreneur() != entrepreneur) {
      throw new CustomException(NO_AUTH_ON_STORE);
    }

    List<Long> curCategoryIds = storeCategoryRepository.findCategoryIdsByStore(store);
    AtomicInteger count = new AtomicInteger();

    request.getCategoryIds().forEach(id -> {
      if (curCategoryIds.contains(id)) {
        log.info("this store is already enrolled on this category."
            + " storeId -> {}, categoryId -> {}", store.getId(), id);

        return;
      }
      Optional<Category> category = categoryRepository.findById(id);

      //카테고리로 조회된 게 없는 경우
      if (category.isEmpty()) {
        log.error("failed to enroll store on category cause there's no category found by id."
            + " storeId -> {}, categoryId -> {} ", store.getId(), id);
      } else {
        storeCategoryRepository.save(StoreCategory.builder()
            .store(store)
            .category(category.get())
            .build());

        count.getAndIncrement();
      }
    });

    return count.get();
  }

  @Override
  @Transactional
  public int deleteOnCategory(Long entrepreneurId, Long storeId, IdsRequest request) {
    Entrepreneur entrepreneur = entrepreneurRepository
        .findByIdAndDeletedAtIsNull(entrepreneurId).orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND));

    Store store = storeRepository
        .findByIdAndDeletedAtIsNull(storeId).orElseThrow(() -> new CustomException(STORE_NOT_FOUND));

    if (store.getEntrepreneur() != entrepreneur) {
      throw new CustomException(NO_AUTH_ON_STORE);
    }

    return storeCategoryRepository.deleteByStoreAndCategory_IdIn(store, request.getCategoryIds());
  }

  @Override
  @Transactional
  public int enrollSchoolsToStore(Long entrepreneurId, Long storeId, SchoolDto.IdsRequest request) {
    Entrepreneur entrepreneur = entrepreneurRepository
        .findByIdAndDeletedAtIsNull(entrepreneurId).orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND));

    Store store = storeRepository
        .findByIdAndDeletedAtIsNull(storeId).orElseThrow(() -> new CustomException(STORE_NOT_FOUND));

    if (store.getEntrepreneur() != entrepreneur) {
      throw new CustomException(NO_AUTH_ON_STORE);
    }

    List<Long> curSchoolIds = storeSchoolRepository.findSchoolIdsByStore(store);
    AtomicInteger count = new AtomicInteger();

    request.getSchoolIds().forEach(id -> {
      if (curSchoolIds.contains(id)) {
        log.info("this school is already enrolled on this store."
            + " schoolId -> {}, storeId -> {}", id, store.getId());

        return;
      }
      Optional<School> school = schoolRepository.findById(id);

      //카테고리로 조회된 게 없는 경우
      if (school.isEmpty()) {
        log.error("failed to enroll school on store cause there's no school found by id."
            + " schoolId -> {}, storeId -> {} ", id, store.getId());
      } else {
        storeSchoolRepository.save(StoreSchool.builder()
            .store(store)
            .school(school.get())
            .build());

        count.getAndIncrement();
      }
    });

    return count.get();
  }

  @Override
  @Transactional
  public int deleteSchoolsOnStore(Long entrepreneurId, Long storeId, SchoolDto.IdsRequest request) {
    Entrepreneur entrepreneur = entrepreneurRepository
        .findByIdAndDeletedAtIsNull(entrepreneurId).orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND));

    Store store = storeRepository
        .findByIdAndDeletedAtIsNull(storeId).orElseThrow(() -> new CustomException(STORE_NOT_FOUND));

    if (store.getEntrepreneur() != entrepreneur) {
      throw new CustomException(NO_AUTH_ON_STORE);
    }

    return storeSchoolRepository.deleteByStoreAndSchool_IdIn(store, request.getSchoolIds());
  }

  @Override
  @Transactional
  public int enrollHolidaysToStore(Long entrepreneurId, Long storeId, HolidaysRequest request) {
    Entrepreneur entrepreneur = entrepreneurRepository
        .findByIdAndDeletedAtIsNull(entrepreneurId).orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND));

    Store store = storeRepository
        .findByIdAndDeletedAtIsNull(storeId).orElseThrow(() -> new CustomException(STORE_NOT_FOUND));

    if (store.getEntrepreneur() != entrepreneur) {
      throw new CustomException(NO_AUTH_ON_STORE);
    }

    List<DayOfWeek> curHolidays = holidayRepository.findHolidaysByStore(store);
    AtomicInteger count = new AtomicInteger();

    request.getHolidays().forEach(dayOfWeek -> {
      if (curHolidays.contains(dayOfWeek)) {
        log.info("this day of week is already enrolled on this store."
            + " dayOfWeek -> {}, storeId -> {}", dayOfWeek, store.getId());

        return;
      }

      holidayRepository.save(Holiday.builder()
              .store(store)
              .dayOfWeek(dayOfWeek)
          .build());

      count.getAndIncrement();
    });

    return count.get();
  }

  @Override
  @Transactional
  public int deleteHolidaysOnStore(Long entrepreneurId, Long storeId, HolidaysRequest request) {
    Entrepreneur entrepreneur = entrepreneurRepository
        .findByIdAndDeletedAtIsNull(entrepreneurId).orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND));

    Store store = storeRepository
        .findByIdAndDeletedAtIsNull(storeId).orElseThrow(() -> new CustomException(STORE_NOT_FOUND));

    if (store.getEntrepreneur() != entrepreneur) {
      throw new CustomException(NO_AUTH_ON_STORE);
    }

    return holidayRepository.deleteByStoreAndDayOfWeekIn(store, request.getHolidays());
  }
}
