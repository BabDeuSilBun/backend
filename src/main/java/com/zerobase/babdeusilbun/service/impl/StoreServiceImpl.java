package com.zerobase.babdeusilbun.service.impl;

import static com.zerobase.babdeusilbun.dto.StoreDto.CreateRequest;
import static com.zerobase.babdeusilbun.dto.StoreDto.IdResponse;
import static com.zerobase.babdeusilbun.dto.StoreDto.ImageUrl;
import static com.zerobase.babdeusilbun.dto.StoreDto.PrincipalInformation;
import static com.zerobase.babdeusilbun.exception.ErrorCode.ALREADY_EXIST_STORE;
import static com.zerobase.babdeusilbun.exception.ErrorCode.ENTREPRENEUR_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.NO_AUTH_ON_PURCHASE;
import static com.zerobase.babdeusilbun.exception.ErrorCode.NO_AUTH_ON_STORE;
import static com.zerobase.babdeusilbun.exception.ErrorCode.NO_IMAGE_ON_STORE;
import static com.zerobase.babdeusilbun.exception.ErrorCode.STORE_IMAGE_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.STORE_NOT_FOUND;
import static com.zerobase.babdeusilbun.util.ImageUtility.STORE_IMAGE_FOLDER;
import static com.zerobase.babdeusilbun.util.MeetingUtility.CAN_ENTREPRENEUR_CHECK_PURCHASE_STATUS;

import com.zerobase.babdeusilbun.component.ImageComponent;
import com.zerobase.babdeusilbun.domain.Category;
import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.Holiday;
import com.zerobase.babdeusilbun.domain.School;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.StoreCategory;
import com.zerobase.babdeusilbun.domain.StoreImage;
import com.zerobase.babdeusilbun.domain.StoreSchool;
import com.zerobase.babdeusilbun.dto.CategoryDto;
import com.zerobase.babdeusilbun.dto.CategoryDto.IdsRequest;
import com.zerobase.babdeusilbun.dto.CategoryDto.Information;
import com.zerobase.babdeusilbun.dto.EntrepreneurDto;
import com.zerobase.babdeusilbun.dto.HolidayDto;
import com.zerobase.babdeusilbun.dto.HolidayDto.HolidaysRequest;
import com.zerobase.babdeusilbun.dto.MenuDto;
import com.zerobase.babdeusilbun.dto.PurchaseDto.MeetingPurchaseResponse;
import com.zerobase.babdeusilbun.dto.SchoolDto;
import com.zerobase.babdeusilbun.dto.StoreCategoryDto;
import com.zerobase.babdeusilbun.dto.StoreDto;
import com.zerobase.babdeusilbun.dto.StoreImageDto;
import com.zerobase.babdeusilbun.dto.StoreImageDto.Thumbnail;
import com.zerobase.babdeusilbun.dto.StoreImageDto.UpdateRequest;
import com.zerobase.babdeusilbun.dto.StoreSchoolDto;
import com.zerobase.babdeusilbun.enums.MeetingStatus;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.repository.CategoryRepository;
import com.zerobase.babdeusilbun.repository.EntrepreneurRepository;
import com.zerobase.babdeusilbun.repository.HolidayRepository;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.MenuRepository;
import com.zerobase.babdeusilbun.repository.SchoolRepository;
import com.zerobase.babdeusilbun.repository.StoreCategoryRepository;
import com.zerobase.babdeusilbun.repository.StoreImageRepository;
import com.zerobase.babdeusilbun.repository.StoreRepository;
import com.zerobase.babdeusilbun.repository.StoreSchoolRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.service.StoreService;
import io.micrometer.common.util.StringUtils;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@AllArgsConstructor
public class StoreServiceImpl implements StoreService {
  private final MeetingServiceImpl meetingService;

  private final UserRepository userRepository;
  private final EntrepreneurRepository entrepreneurRepository;
  private final StoreRepository storeRepository;
  private final StoreImageRepository imageRepository;
  private final SchoolRepository schoolRepository;
  private final StoreSchoolRepository storeSchoolRepository;
  private final CategoryRepository categoryRepository;
  private final StoreCategoryRepository storeCategoryRepository;
  private final MenuRepository menuRepository;
  private final HolidayRepository holidayRepository;
  private final MeetingRepository meetingRepository;
  private final ImageComponent imageComponent;

  private record EntrepreneurStoreImageData(Entrepreneur entrepreneur, Store store, StoreImage image) {}

  private EntrepreneurStoreImageData getEntrepreneurAndStoreAndImage(Long entrepreneurId, Long storeId) {
    Entrepreneur entrepreneur = entrepreneurRepository
        .findByIdAndDeletedAtIsNull(entrepreneurId)
        .orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND));

    Store store = storeRepository
        .findByIdAndDeletedAtIsNull(storeId)
        .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));

    if (store.getEntrepreneur() != entrepreneur) {
      throw new CustomException(NO_AUTH_ON_STORE);
    }

    return new EntrepreneurStoreImageData(entrepreneur, store, null);
  }

  private EntrepreneurStoreImageData getEntrepreneurAndStoreAndImage(Long entrepreneurId, Long storeId, Long imageId) {
    Entrepreneur entrepreneur = entrepreneurRepository
        .findByIdAndDeletedAtIsNull(entrepreneurId)
        .orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND));

    Store store = storeRepository
        .findByIdAndDeletedAtIsNull(storeId)
        .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));

    StoreImage image = imageRepository.findById(imageId)
        .orElseThrow(() -> new CustomException(STORE_IMAGE_NOT_FOUND));

    if (store.getEntrepreneur() != entrepreneur) {
      throw new CustomException(NO_AUTH_ON_STORE);
    }

    if (image.getStore() != store) {
      throw new CustomException(NO_IMAGE_ON_STORE);
    }

    return new EntrepreneurStoreImageData(entrepreneur, store, image);
  }

  @Override
  @Transactional
  public IdResponse createStore(Long entrepreneurId, CreateRequest request) {
    Entrepreneur entrepreneur = entrepreneurRepository
        .findByIdAndDeletedAtIsNull(entrepreneurId)
        .orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND));

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
    EntrepreneurStoreImageData data = getEntrepreneurAndStoreAndImage(entrepreneurId, storeId);
    Store store = data.store();

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
    EntrepreneurStoreImageData data = getEntrepreneurAndStoreAndImage(entrepreneurId, storeId);
    Store store = data.store();

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
    EntrepreneurStoreImageData data = getEntrepreneurAndStoreAndImage(entrepreneurId, storeId);
    Store store = data.store();

    return storeCategoryRepository.deleteByStoreAndCategory_IdIn(store, request.getCategoryIds());
  }

  @Override
  @Transactional
  public int enrollSchoolsToStore(Long entrepreneurId, Long storeId, SchoolDto.IdsRequest request) {
    EntrepreneurStoreImageData data = getEntrepreneurAndStoreAndImage(entrepreneurId, storeId);
    Store store = data.store();

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
    EntrepreneurStoreImageData data = getEntrepreneurAndStoreAndImage(entrepreneurId, storeId);
    Store store = data.store();

    return storeSchoolRepository.deleteByStoreAndSchool_IdIn(store, request.getSchoolIds());
  }

  @Override
  @Transactional
  public int enrollHolidaysToStore(Long entrepreneurId, Long storeId, HolidaysRequest request) {
    EntrepreneurStoreImageData data = getEntrepreneurAndStoreAndImage(entrepreneurId, storeId);
    Store store = data.store();

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
    EntrepreneurStoreImageData data = getEntrepreneurAndStoreAndImage(entrepreneurId, storeId);
    Store store = data.store();

    return holidayRepository.deleteByStoreAndDayOfWeekIn(store, request.getHolidays());
  }

  @Override
  @Transactional
  public boolean deleteImageOnStore(Long entrepreneurId, Long storeId, Long imageId) {
    EntrepreneurStoreImageData data = getEntrepreneurAndStoreAndImage(entrepreneurId, storeId, imageId);
    Store store = data.store();
    StoreImage image = data.image();

    AtomicInteger count = new AtomicInteger();
    imageRepository.findAllByStoreOrderBySequenceAsc(store)
        .forEach(storeImage -> {
          if (!Objects.equals(storeImage.getId(), imageId)) {
            storeImage.update(image.getIsRepresentative() && count.get() == 0, count.getAndIncrement());

            return;
          }

          imageRepository.delete(storeImage);
        });

    try {
      imageComponent.deleteImageByUrl(image.getUrl());
    } catch (CustomException e) {
      log.error("failed to delete image on S3. imageURL -> {} ", image.getUrl());

      return false;
    }

    return true;
  }

  @Override
  @Transactional
  public void updateStoreImage(Long entrepreneurId, Long storeId, Long imageId, UpdateRequest request) {
    EntrepreneurStoreImageData data = getEntrepreneurAndStoreAndImage(entrepreneurId, storeId, imageId);
    Store store = data.store();
    StoreImage image = data.image();

    //값 유효성 확인
    if (!Objects.equals(request.getSequence(), image.getSequence())) {
      request.setSequence(null);
    }
    if (!Objects.equals(request.getIsRepresentative(), image.getIsRepresentative())) {
      request.setIsRepresentative(null);
    }

    List<StoreImage> images = imageRepository.findAllByStoreOrderBySequenceAsc(store);
    if (request.getSequence() != null) {
      images.remove((int) image.getSequence()-1);
      images.add(request.getSequence()-1, image);

      sortImagesByNewSequence(images);
    }

    if (request.getIsRepresentative() != null) {
      updateImageRepresentative(image, images);
    }
  }

  private void sortImagesByNewSequence(List<StoreImage> images) {
    for (int i = 0; i < images.size(); i++) {
      StoreImage image = images.get(i);

      if (image.getSequence() != i+1) {
        image.setSequence(i+1);
      }
    }
  }

  private void updateImageRepresentative(StoreImage cur, List<StoreImage> images) {
    boolean curRepresentative = cur.getIsRepresentative();

    for(StoreImage image: images) {
      if (image != cur && image.getIsRepresentative() != curRepresentative) {
        image.setIsRepresentative(!image.getIsRepresentative());

        break;
      }
    }

    cur.setIsRepresentative(!cur.getIsRepresentative());
  }

  @Override
  @Transactional
  public void updateStoreInformation(Long entrepreneurId, Long storeId, StoreDto.UpdateRequest request) {
    EntrepreneurStoreImageData data = getEntrepreneurAndStoreAndImage(entrepreneurId, storeId);
    Store store = data.store();

    if (request.getCategoryIds() != null) {
      enrollToCategory(entrepreneurId, storeId,
          new CategoryDto.IdsRequest(request.getCategoryIds()));

      storeCategoryRepository.deleteByStoreAndCategory_IdNotIn(store, request.getCategoryIds());
    }

    if (request.getSchoolIds() != null) {
      enrollSchoolsToStore(entrepreneurId, storeId,
          new SchoolDto.IdsRequest(request.getSchoolIds()));

      storeSchoolRepository.deleteByStoreAndSchool_IdNotIn(store, request.getSchoolIds());
    }

    store.update(request);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<StoreDto.Information> getAvailStoreList(
      Long userId, List<Long> categoryList, String searchMenu,
      Long schoolId, String sortCriteria, Pageable pageable) {

    if (categoryList == null) {
      categoryList = Collections.emptyList();
    }

    if (schoolId == null || schoolId == 0L) {
      schoolId = userRepository.findByIdAndDeletedAtIsNull(userId)
          .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))
          .getSchool().getId();
    }

    return storeRepository
        .getAvailStoreList(categoryList, searchMenu, schoolId, sortCriteria, pageable)
        .map(this::mapToStoreDto);
  }

  private StoreDto.Information mapToStoreDto(Store store) {
    return StoreDto.Information.fromEntity(
        store, imageRepository.findAllByStoreOrderBySequenceAsc(store)
    );
  }

  @Override
  @Transactional
  public void deleteStore(Long entrepreneurId, Long storeId) {
    EntrepreneurStoreImageData data = getEntrepreneurAndStoreAndImage(entrepreneurId, storeId);
    Store store = data.store();

    store.delete();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<StoreDto.SimpleInformation> getAllStoresByEntrepreneur(
      Long entrepreneurId, int page, int size, boolean unprocessedOnly) {
    Entrepreneur entrepreneur = entrepreneurRepository
        .findByIdAndDeletedAtIsNull(entrepreneurId).orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND));

    Long count = storeRepository.getStoresCountByEntrepreneur(entrepreneur, unprocessedOnly);
    if (count == null || count == 0L) {
      return new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 1), 0);
    }

    size = (size <= 0) ? count.intValue() : size;
    page = Math.min(page, ((int) Math.ceil((double) count / size))-1);

    Pageable pageable = PageRequest.of(page, size, Sort.by(Order.asc("name")));

    return storeRepository.getStorePageByEntrepreneur(entrepreneur, pageable, unprocessedOnly);
  }

  @Override
  @Transactional(readOnly = true)
  public PrincipalInformation getStore(Long storeId) {
    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));

    return PrincipalInformation.fromEntity(store);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<HolidayDto.Information> getAllHolidays(Long storeId, int page, int size) {
    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));

    int count = holidayRepository.countByStore(store);
    if (count == 0) {
      return new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 1), 0);
    }

    size = (size <= 0) ? count : size;
    page = Math.min(page, ((int) Math.ceil((double) count / size))-1);

    Pageable pageable = PageRequest.of(page, size,
        Sort.by(Order.asc("dayOfWeek").with(Sort.NullHandling.NATIVE)));

    return holidayRepository.findByStoreOrderByDayOfWeek(store, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<StoreCategoryDto.Information> getAllCategories(Long storeId, int page, int size) {
    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));

    int count = storeCategoryRepository.countByStore(store);
    if (count == 0) {
      return new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 1), 0);
    }

    size = (size <= 0) ? count : size;
    page = Math.min(page, ((int) Math.ceil((double) count / size))-1);

    Pageable pageable = PageRequest.of(page, size, Sort.by(Order.asc("category.name")));

    return storeCategoryRepository.findByStore(store, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<MenuDto.Information> getAllMenus(Long storeId, int page, int size) {
    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));

    int count = menuRepository.countByStoreAndDeletedAtIsNull(store);
    if (count == 0) {
      return new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 1), 0);
    }

    size = (size <= 0) ? count : size;
    page = Math.min(page, ((int) Math.ceil((double) count / size))-1);

    Pageable pageable = PageRequest.of(page, size, Sort.by(Order.asc("id")));

    return menuRepository.findByStoreAndDeletedAtIsNull(store, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public EntrepreneurDto.SimpleInformation getEntrepreneur(Long storeId) {
    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));

    if (store.getEntrepreneur() == null || store.getEntrepreneur().getDeletedAt() != null) {
      return null;
    }

    return EntrepreneurDto.SimpleInformation.fromEntity(store.getEntrepreneur());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<StoreSchoolDto.Information> getAllSchools(Long storeId, int page, int size) {
    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));

    int count = storeSchoolRepository.countByStore(store);
    if (count == 0) {
      return new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 1), 0);
    }

    size = (size <= 0) ? count : size;
    page = Math.min(page, ((int) Math.ceil((double) count / size))-1);

    Pageable pageable = PageRequest.of(
        page, size, Sort.by(Order.asc("school.name"), Order.asc("school.campus")));

    return storeSchoolRepository.findByStore(store, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<StoreImageDto.Information> getAllImages(Long storeId, int page, int size) {
    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));

    int count = imageRepository.countByStore(store);
    if (count == 0) {
      return new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 1), 0);
    }

    size = (size <= 0) ? count : size;
    page = Math.min(page, ((int) Math.ceil((double) count / size))-1);

    Pageable pageable = PageRequest.of(page, size, Sort.by(Order.asc("sequence")));

    return imageRepository.findByStore(store, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Thumbnail getThumbnail(Long storeId) {
    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));

    return imageRepository.findFirstByStoreAndIsRepresentativeTrue(store).orElse(null);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<MeetingPurchaseResponse> getAllMeetingPurchaseByStoreId(Long entrepreneurId, Long storeId, String status,
      int page, int size, int menuPage, int menuSize) {
    EntrepreneurStoreImageData data = getEntrepreneurAndStoreAndImage(entrepreneurId, storeId);
    Store store = data.store();

    MeetingStatus meetingStatus = (StringUtils.isBlank(status)) ? null : MeetingStatus.valueOf(status);
    if (meetingStatus != null && !CAN_ENTREPRENEUR_CHECK_PURCHASE_STATUS.contains(meetingStatus)) {
      throw new CustomException(NO_AUTH_ON_PURCHASE); //주문 내역을 볼 수 있는 권한이 있는 상태가 아님
    }

    int count = meetingRepository.countByStoreAndStatusInAndDeletedAtIsNull(
        store, (meetingStatus == null) ? CAN_ENTREPRENEUR_CHECK_PURCHASE_STATUS : List.of(meetingStatus));
    if (count == 0) {
      return new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 1), 0);
    }

    size = (size <= 0) ? count : size;
    page = Math.min(page, ((int) Math.ceil((double) count / size))-1);

    Pageable pageable = PageRequest.of(page, size, Sort.by(
        Order.desc("meetingPurchaseTime.createdAt").with(Sort.NullHandling.NATIVE)));

    return meetingRepository.findAllByStoreAndStatusInAndDeletedAtIsNullOrderByPurchaseTimeCreatedAtDesc(
        store, (meetingStatus == null) ? CAN_ENTREPRENEUR_CHECK_PURCHASE_STATUS : List.of(meetingStatus), pageable
    ).map(
        meeting -> MeetingPurchaseResponse.fromEntity(
            meeting, meetingService.getMenuByMeetingAndStatus(meeting, menuPage, menuSize)
        )
    );
  }
}
