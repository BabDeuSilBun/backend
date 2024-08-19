package com.zerobase.babdeusilbun.service;

import static com.zerobase.babdeusilbun.exception.ErrorCode.ALREADY_EXIST_STORE;
import static com.zerobase.babdeusilbun.exception.ErrorCode.ENTREPRENEUR_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.FAILED_DELETE_FILE;
import static com.zerobase.babdeusilbun.exception.ErrorCode.NO_AUTH_ON_STORE;
import static com.zerobase.babdeusilbun.exception.ErrorCode.NO_IMAGE_ON_STORE;
import static com.zerobase.babdeusilbun.exception.ErrorCode.STORE_IMAGE_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.STORE_NOT_FOUND;
import static com.zerobase.babdeusilbun.util.ImageUtility.STORE_IMAGE_FOLDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.zerobase.babdeusilbun.component.ImageComponent;
import com.zerobase.babdeusilbun.domain.Category;
import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.Holiday;
import com.zerobase.babdeusilbun.domain.School;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.StoreCategory;
import com.zerobase.babdeusilbun.domain.StoreImage;
import com.zerobase.babdeusilbun.domain.StoreSchool;
import com.zerobase.babdeusilbun.dto.AddressDto;
import com.zerobase.babdeusilbun.dto.CategoryDto.IdsRequest;
import com.zerobase.babdeusilbun.dto.CategoryDto.Information;
import com.zerobase.babdeusilbun.dto.HolidayDto.HolidaysRequest;
import com.zerobase.babdeusilbun.dto.SchoolDto;
import com.zerobase.babdeusilbun.dto.StoreDto;
import com.zerobase.babdeusilbun.dto.StoreDto.CreateRequest;
import com.zerobase.babdeusilbun.dto.StoreImageDto;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.repository.CategoryRepository;
import com.zerobase.babdeusilbun.repository.EntrepreneurRepository;
import com.zerobase.babdeusilbun.repository.HolidayRepository;
import com.zerobase.babdeusilbun.repository.SchoolRepository;
import com.zerobase.babdeusilbun.repository.StoreCategoryRepository;
import com.zerobase.babdeusilbun.repository.StoreImageRepository;
import com.zerobase.babdeusilbun.repository.StoreRepository;
import com.zerobase.babdeusilbun.repository.StoreSchoolRepository;
import com.zerobase.babdeusilbun.service.impl.StoreServiceImpl;
import com.zerobase.babdeusilbun.util.TestEntrepreneurUtility;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {
  @Mock
  private EntrepreneurRepository entrepreneurRepository;

  @Mock
  private StoreRepository storeRepository;

  @Mock
  private StoreImageRepository imageRepository;

  @Mock
  private SchoolRepository schoolRepository;

  @Mock
  private StoreSchoolRepository storeSchoolRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private StoreCategoryRepository storeCategoryRepository;

  @Mock
  private HolidayRepository holidayRepository;

  @Mock
  private ImageComponent imageComponent;

  @InjectMocks
  private StoreServiceImpl storeService;

  private final List<Category> categories = List.of(
      Category.builder().id(1L).name("야식").build(),
      Category.builder().id(2L).name("점심").build(),
      Category.builder().id(3L).name("저녁").build()
  );

  private final CreateRequest createRequest = CreateRequest.builder()
      .name("가짜 상점")
      .description("가짜 상점입니다.")
      .minPurchasePrice(10000L)
      .minDeliveryTime(30)
      .maxDeliveryTime(60)
      .deliveryPrice(1000L)
      .address(new AddressDto("00000", "도로명주소", "상세주소"))
      .phoneNumber("01012345678")
      .openTime(LocalTime.of(9, 0))
      .closeTime(LocalTime.of(21, 0))
      .build();

  @DisplayName("가게 생성 성공 사례 테스트")
  @Test
  void createStoreSuccess() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store expected = createRequest.toEntity(entrepreneur);

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(TestEntrepreneurUtility.getEntrepreneur().getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.existsByEntrepreneurAndNameAndAddressAndDeletedAtIsNull(
        eq(entrepreneur), eq(createRequest.getName()), any()))
        .thenReturn(false);
    when(storeRepository.save(any(Store.class))).thenReturn(expected);

    ArgumentCaptor<Store> storeCaptor = ArgumentCaptor.forClass(Store.class);

    //then
    Long storeId = storeService.createStore(entrepreneur.getId(), createRequest).getStoreId();

    verify(storeRepository, times(1)).save(storeCaptor.capture());

    assertEquals(expected.getId(), storeId);
    assertEquals(
        expected.getEntrepreneur().getId(), storeCaptor.getValue().getEntrepreneur().getId());
    assertEquals(expected.getName(), storeCaptor.getValue().getName());
    assertEquals(AddressDto.fromEntity(expected.getAddress()),
        AddressDto.fromEntity(storeCaptor.getValue().getAddress()));
  }

  @DisplayName("가게 생성 실패(중복된 가게 등록 요청)")
  @Test
  void createStoreFailedAlreadyExists() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId()))).thenReturn(Optional.of(entrepreneur));
    when(storeRepository.existsByEntrepreneurAndNameAndAddressAndDeletedAtIsNull(
        eq(entrepreneur), eq(createRequest.getName()), any())).thenReturn(true);

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.createStore(entrepreneur.getId(), createRequest);
    });

    assertEquals(ALREADY_EXIST_STORE, exception.getErrorCode());
  }

  @DisplayName("가게 생성 실패(사업가 미존재)")
  @Test
  void createStoreFailedEntrepreneurNotFound() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.empty());

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.createStore(entrepreneur.getId(), createRequest);
    });

    assertEquals(ENTREPRENEUR_NOT_FOUND, exception.getErrorCode());
  }

  @DisplayName("이미지 업로드 성공 사례 테스트")
  @Test
  void uploadImageToStoreSuccess() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(entrepreneur);
    List<MultipartFile> images = List.of();

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));
    when(imageComponent.uploadImageList(eq(images), eq(STORE_IMAGE_FOLDER)))
        .thenReturn(List.of("url1", "url2"));
    when(imageRepository.countByStore(eq(store))).thenReturn(0);

    ArgumentCaptor<List<StoreImage>> storeImagesCaptor = ArgumentCaptor.forClass(List.class);

    //then
    int uploadedImageCount = storeService.uploadImageToStore(entrepreneur.getId(), images, store.getId());
    verify(imageRepository, times(1)).saveAll(storeImagesCaptor.capture());

    assertEquals(2, uploadedImageCount);
    List<StoreImage> savedImages = storeImagesCaptor.getValue();
    assertEquals("url1", savedImages.get(0).getUrl());
    assertTrue(savedImages.get(0).getIsRepresentative());
    assertEquals("url2", savedImages.get(1).getUrl());
    assertFalse(savedImages.get(1).getIsRepresentative());
  }

  @DisplayName("카테고리 조회 성공")
  @Test
  void getAllCategoriesSuccess() {
    //given
    Pageable pageable = PageRequest.of(0, 10);
    List<Information> informationList = categories.stream().map(Information::fromEntity).toList();

    Page<Information> categoryPage = new PageImpl<>(informationList, pageable, categories.size());

    //when
    when(categoryRepository.getAllCategories(eq(pageable.getPageNumber()), eq(pageable.getPageSize())))
        .thenReturn(categoryPage);

    Page<Information> result = storeService.getAllCategories(pageable.getPageNumber(), pageable.getPageSize());

    //then
    assertEquals(categoryPage, result);
    assertEquals(categories.size(), result.getTotalElements());
  }

  @DisplayName("상점에 카테고리 등록 성공")
  @Test
  void enrollToCategorySuccess() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(entrepreneur);

    List<Long> existingCategoryIds = List.of(1L);
    IdsRequest idsRequest = new IdsRequest(Set.of(2L, 3L));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));
    when(storeCategoryRepository.findCategoryIdsByStore(eq(store)))
        .thenReturn(existingCategoryIds);
    when(categoryRepository.findById(2L))
        .thenReturn(Optional.of(Category.builder().id(2L).name("2L의 카테고리").build()));
    when(categoryRepository.findById(3L))
        .thenReturn(Optional.of(Category.builder().id(3L).name("2L의 카테고리").build()));

    int successCount = storeService.enrollToCategory(entrepreneur.getId(), store.getId(), idsRequest);

    //then
    ArgumentCaptor<StoreCategory> storeCategoryCaptor = ArgumentCaptor.forClass(StoreCategory.class);
    verify(storeCategoryRepository, times(2)).save(storeCategoryCaptor.capture());

    assertEquals(2, successCount);

    verify(storeCategoryRepository, times(2)).save(any(StoreCategory.class));
  }

  @DisplayName("상점에 카테고리 등록 실패(상점 미존재)")
  @Test
  void enrollToCategoryFailedStoreNotFound() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    IdsRequest idsRequest = new IdsRequest(Set.of(2L));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(1L)))
        .thenReturn(Optional.empty());

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.enrollToCategory(entrepreneur.getId(), 1L, idsRequest);
    });

    assertEquals(STORE_NOT_FOUND, exception.getErrorCode());
  }

  @DisplayName("상점에 카테고리 등록 실패(로그인한 이용자가 상점주인이 아님)")
  @Test
  void enrollToCategoryFailedNoAuth() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(new Entrepreneur());

    IdsRequest idsRequest = new IdsRequest(Set.of(1L));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.enrollToCategory(entrepreneur.getId(), store.getId(), idsRequest);
    });

    assertEquals(NO_AUTH_ON_STORE, exception.getErrorCode());
  }

  @DisplayName("상점에 카테고리 삭제 성공")
  @Test
  void deleteOnCategorySuccess() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(entrepreneur);
    IdsRequest idsRequest = new IdsRequest(Set.of(1L, 2L));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));
    when(storeCategoryRepository.deleteByStoreAndCategory_IdIn(eq(store), eq(idsRequest.getCategoryIds())))
        .thenReturn(2);

    //then
    int deletedCount = storeService.deleteOnCategory(entrepreneur.getId(), store.getId(), idsRequest);

    assertEquals(2, deletedCount);
    verify(storeCategoryRepository, times(1))
        .deleteByStoreAndCategory_IdIn(eq(store), eq(idsRequest.getCategoryIds()));
  }

  @DisplayName("상점에 카테고리 삭제 실패(상점 미존재)")
  @Test
  void deleteOnCategoryFailedStoreNotFound() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    IdsRequest idsRequest = new IdsRequest(Set.of(2L));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(1L)))
        .thenReturn(Optional.empty());

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.deleteOnCategory(entrepreneur.getId(), 1L, idsRequest);
    });

    assertEquals(STORE_NOT_FOUND, exception.getErrorCode());
  }

  @DisplayName("상점에 카테고리 삭제 실패(로그인한 이용자가 상점주인이 아님)")
  @Test
  void deleteOnCategoryFailedNoAuth() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(new Entrepreneur());

    IdsRequest idsRequest = new IdsRequest(Set.of(1L));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.deleteOnCategory(entrepreneur.getId(), store.getId(), idsRequest);
    });

    assertEquals(NO_AUTH_ON_STORE, exception.getErrorCode());
  }

  @DisplayName("상점에 학교 등록 성공")
  @Test
  void enrollSchoolsToStoreSuccess() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(entrepreneur);
    List<Long> existingSchoolIds = List.of(1L);
    SchoolDto.IdsRequest idsRequest = new SchoolDto.IdsRequest(Set.of(2L, 3L));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));
    when(storeSchoolRepository.findSchoolIdsByStore(eq(store)))
        .thenReturn(existingSchoolIds);
    when(schoolRepository.findById(2L))
        .thenReturn(Optional.of(School.builder().id(2L).name("2L의 학교").build()));
    when(schoolRepository.findById(3L))
        .thenReturn(Optional.of(School.builder().id(3L).name("3L의 학교").build()));
    
    ArgumentCaptor<StoreSchool> storeSchoolCaptor = ArgumentCaptor.forClass(StoreSchool.class);

    //then
    int successCount = storeService.enrollSchoolsToStore(entrepreneur.getId(), store.getId(), idsRequest);
    verify(storeSchoolRepository, times(2)).save(storeSchoolCaptor.capture());

    assertEquals(2, successCount);

    verify(storeSchoolRepository, times(2)).save(any(StoreSchool.class));
  }

  @DisplayName("상점에 학교 등록 실패(상점 미존재)")
  @Test
  void enrollSchoolsToStoreFailedStoreNotFound() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    SchoolDto.IdsRequest idsRequest = new SchoolDto.IdsRequest(Set.of(2L));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(1L)))
        .thenReturn(Optional.empty());

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.enrollSchoolsToStore(entrepreneur.getId(), 1L, idsRequest);
    });

    assertEquals(STORE_NOT_FOUND, exception.getErrorCode());
  }

  @DisplayName("상점에 학교 등록 실패(로그인한 이용자가 상점주인이 아님)")
  @Test
  void enrollSchoolsToStoreFailedNoAuth() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(new Entrepreneur());
    SchoolDto.IdsRequest idsRequest = new SchoolDto.IdsRequest(Set.of(1L));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.enrollSchoolsToStore(entrepreneur.getId(), store.getId(), idsRequest);
    });

    assertEquals(NO_AUTH_ON_STORE, exception.getErrorCode());
  }

  @DisplayName("상점에서 학교 삭제 성공")
  @Test
  void deleteSchoolsOnStoreSuccess() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(entrepreneur);
    SchoolDto.IdsRequest idsRequest = new SchoolDto.IdsRequest(Set.of(1L, 2L));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));
    when(storeSchoolRepository.deleteByStoreAndSchool_IdIn(eq(store), eq(idsRequest.getSchoolIds())))
        .thenReturn(2);

    //then
    int deletedCount = storeService.deleteSchoolsOnStore(entrepreneur.getId(), store.getId(), idsRequest);

    assertEquals(2, deletedCount);
    verify(storeSchoolRepository, times(1))
        .deleteByStoreAndSchool_IdIn(eq(store), eq(idsRequest.getSchoolIds()));
  }

  @DisplayName("상점에서 학교 삭제 실패(상점 미존재)")
  @Test
  void deleteSchoolsOnStoreFailedStoreNotFound() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    SchoolDto.IdsRequest idsRequest = new SchoolDto.IdsRequest(Set.of(2L));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(1L)))
        .thenReturn(Optional.empty());

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.deleteSchoolsOnStore(entrepreneur.getId(), 1L, idsRequest);
    });

    assertEquals(STORE_NOT_FOUND, exception.getErrorCode());
  }

  @DisplayName("상점에서 학교 삭제 실패(로그인한 이용자가 상점주인이 아님)")
  @Test
  void deleteSchoolsOnStoreFailedNoAuth() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(new Entrepreneur());
    SchoolDto.IdsRequest idsRequest = new SchoolDto.IdsRequest(Set.of(1L));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.deleteSchoolsOnStore(entrepreneur.getId(), store.getId(), idsRequest);
    });

    assertEquals(NO_AUTH_ON_STORE, exception.getErrorCode());
  }

  @DisplayName("휴무일 등록 성공 사례 테스트")
  @Test
  void enrollHolidaysToStoreSuccess() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(entrepreneur);
    HolidaysRequest request = new HolidaysRequest(Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));
    when(holidayRepository.findHolidaysByStore(eq(store)))
        .thenReturn(List.of(DayOfWeek.WEDNESDAY));

    int successCount = storeService.enrollHolidaysToStore(entrepreneur.getId(), store.getId(), request);
    ArgumentCaptor<Holiday> holidayCaptor = ArgumentCaptor.forClass(Holiday.class);

    //then
    assertEquals(2, successCount);
    verify(holidayRepository, times(2)).save(holidayCaptor.capture());

    List<Holiday> savedHolidays = holidayCaptor.getAllValues();
    assertTrue(savedHolidays.stream()
        .anyMatch(holiday -> holiday.getDayOfWeek().equals(DayOfWeek.TUESDAY)));
    assertTrue(savedHolidays.stream()
        .anyMatch(holiday -> holiday.getDayOfWeek().equals(DayOfWeek.MONDAY)));
  }

  @DisplayName("휴무일 삭제 성공 사례 테스트")
  @Test
  void deleteHolidaysOnStoreSuccess() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(entrepreneur);
    HolidaysRequest request = new HolidaysRequest(Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));
    when(holidayRepository.deleteByStoreAndDayOfWeekIn(eq(store), eq(request.getHolidays())))
        .thenReturn(request.getHolidays().size());

    int deleteCount = storeService.deleteHolidaysOnStore(entrepreneur.getId(), store.getId(), request);

    //then
    assertEquals(2, deleteCount);
    verify(holidayRepository, times(1))
        .deleteByStoreAndDayOfWeekIn(eq(store), eq(request.getHolidays()));
  }

  @DisplayName("상점에 휴무일 등록 실패(사업가 미존재)")
  @Test
  void enrollHolidaysToStoreFailedEntrepreneurNotFound() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    HolidaysRequest request = new HolidaysRequest(Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.empty());

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.enrollHolidaysToStore(entrepreneur.getId(), 1L, request);
    });

    assertEquals(ENTREPRENEUR_NOT_FOUND, exception.getErrorCode());
  }

  @DisplayName("상점에 휴무일 등록 실패(상점 미존재)")
  @Test
  void enrollHolidaysToStoreFailedStoreNotFound() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    HolidaysRequest request = new HolidaysRequest(Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.empty());

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.enrollHolidaysToStore(entrepreneur.getId(), 1L, request);
    });

    assertEquals(STORE_NOT_FOUND, exception.getErrorCode());
  }

  @DisplayName("상점에 휴무일 등록 실패(해당 상점에 대한 권한 없음)")
  @Test
  void enrollHolidaysToStoreFailedNoAuthOnStore() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(new Entrepreneur());
    HolidaysRequest request = new HolidaysRequest(Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.enrollHolidaysToStore(entrepreneur.getId(), store.getId(), request);
    });

    assertEquals(NO_AUTH_ON_STORE, exception.getErrorCode());
  }

  @DisplayName("상점에서 휴무일 삭제 실패(사업가 미존재)")
  @Test
  void deleteHolidaysOnStoreFailedEntrepreneurNotFound() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    HolidaysRequest request = new HolidaysRequest(Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.empty());

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.deleteHolidaysOnStore(entrepreneur.getId(), 1L, request);
    });

    assertEquals(ENTREPRENEUR_NOT_FOUND, exception.getErrorCode());
  }

  @DisplayName("상점에서 휴무일 삭제 실패(상점 미존재)")
  @Test
  void deleteHolidaysOnStoreFailedStoreNotFound() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    HolidaysRequest request = new HolidaysRequest(Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.empty());

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.deleteHolidaysOnStore(entrepreneur.getId(), 1L, request);
    });

    assertEquals(STORE_NOT_FOUND, exception.getErrorCode());
  }

  @DisplayName("상점에서 휴무일 삭제 실패(해당 상점에 대한 권한 없음)")
  @Test
  void deleteHolidaysOnStoreFailedNoAuthOnStore() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(new Entrepreneur());
    HolidaysRequest request = new HolidaysRequest(Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.deleteHolidaysOnStore(entrepreneur.getId(), store.getId(), request);
    });

    assertEquals(NO_AUTH_ON_STORE, exception.getErrorCode());
  }

  @DisplayName("상점에서 이미지 삭제 성공")
  @Test
  void deleteImageOnStoreSuccess() {
    // given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(entrepreneur);
    StoreImage image = StoreImage.builder()
        .id(1L)
        .url("http://test.com/image.jpg")
        .isRepresentative(true)
        .sequence(0)
        .store(store)
        .build();

    // when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));
    when(imageRepository.findById(eq(1L)))
        .thenReturn(Optional.of(image));
    doNothing().when(imageComponent).deleteImageByUrl(eq(image.getUrl()));
    when(imageRepository.findAllByStoreOrderBySequenceAsc(eq(store)))
        .thenReturn(List.of(image));

    // then
    boolean result = storeService.deleteImageOnStore(entrepreneur.getId(), store.getId(), 1L);

    assertTrue(result);
    verify(imageRepository, times(1)).delete(eq(image));
    verify(imageComponent, times(1)).deleteImageByUrl(eq(image.getUrl()));
  }

  @DisplayName("상점에서 이미지 삭제 실패(상점 미존재)")
  @Test
  void deleteImageOnStoreFailedStoreNotFound() {
    // given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();

    // when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(1L)))
        .thenReturn(Optional.empty());

    // then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.deleteImageOnStore(entrepreneur.getId(), 1L, 1L);
    });

    assertEquals(STORE_NOT_FOUND, exception.getErrorCode());
  }

  @DisplayName("상점에서 이미지 삭제 실패(로그인한 이용자가 상점주인이 아님)")
  @Test
  void deleteImageOnStoreFailedNoAuth() {
    // given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(new Entrepreneur());  // 다른 사업가가 소유한 상점
    StoreImage image = StoreImage.builder()
        .id(1L)
        .url("http://test.com/image.jpg")
        .isRepresentative(false)
        .store(new Store()) // 다른 store 설정
        .build();
    Long imageId = image.getId();

    // when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));
    when(imageRepository.findById(eq(imageId)))
        .thenReturn(Optional.of(image));

    // then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.deleteImageOnStore(entrepreneur.getId(), store.getId(), 1L);
    });

    assertEquals(NO_AUTH_ON_STORE, exception.getErrorCode());
  }

  @DisplayName("상점에서 이미지 삭제 실패(이미지 미존재)")
  @Test
  void deleteImageOnStoreFailedImageNotFound() {
    // given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(entrepreneur);

    // when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));
    when(imageRepository.findById(eq(1L)))
        .thenReturn(Optional.empty());

    // then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.deleteImageOnStore(entrepreneur.getId(), store.getId(), 1L);
    });

    assertEquals(STORE_IMAGE_NOT_FOUND, exception.getErrorCode());
  }

  @DisplayName("상점에서 이미지 삭제 실패(조회한 상점에 등록된 이미지가 아닐 때)")
  @Test
  void deleteImageOnStoreFailedimageNotOnStore() {
    // given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(entrepreneur);
    StoreImage image = StoreImage.builder()
        .id(1L)
        .url("http://test.com/image.jpg")
        .isRepresentative(false)
        .store(new Store()) // 다른 store 설정
        .build();
    Long imageId = image.getId();

    // when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));
    when(imageRepository.findById(eq(imageId)))
        .thenReturn(Optional.of(image));

    // then
    CustomException thrown = assertThrows(CustomException.class, () -> {
      storeService.deleteImageOnStore(entrepreneur.getId(), store.getId(), imageId);
    });

    assertEquals(NO_IMAGE_ON_STORE, thrown.getErrorCode());
  }

  @DisplayName("상점에서 이미지 삭제 실패(S3에서 삭제 실패)")
  @Test
  void deleteImageOnStoreFailedS3Delete() {
    // given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(entrepreneur);
    StoreImage image = StoreImage.builder()
        .id(1L)
        .url("http://test.com/image.jpg")
        .isRepresentative(true)
        .sequence(0)
        .store(store)
        .build();

    // when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));
    when(imageRepository.findById(eq(1L)))
        .thenReturn(Optional.of(image));
    when(imageRepository.findAllByStoreOrderBySequenceAsc(eq(store)))
        .thenReturn(List.of(image));
    doThrow(new CustomException(FAILED_DELETE_FILE)).when(imageComponent).deleteImageByUrl(eq(image.getUrl()));

    // then
    boolean result = storeService.deleteImageOnStore(entrepreneur.getId(), store.getId(), 1L);

    assertFalse(result);
    verify(imageRepository, times(1)).delete(eq(image));
    verify(imageComponent, times(1)).deleteImageByUrl(eq(image.getUrl()));
  }

  @DisplayName("가게 정보 업데이트 성공")
  @Test
  void updateStoreInformationSuccess() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(entrepreneur);
    StoreDto.UpdateRequest request = new StoreDto.UpdateRequest();
    request.setName("변경된 상점명");
    request.setCategoryIds(Set.of(1L, 2L));
    request.setSchoolIds(Set.of(1L, 2L));

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));

    //then
    storeService.updateStoreInformation(entrepreneur.getId(), store.getId(), request);

    verify(storeCategoryRepository, times(1))
        .deleteByStoreAndCategory_IdNotIn(eq(store), eq(request.getCategoryIds()));
    verify(storeSchoolRepository, times(1))
        .deleteByStoreAndSchool_IdNotIn(eq(store), eq(request.getSchoolIds()));
    assertEquals(store.getName(), request.getName());
  }

  @DisplayName("가게 정보 업데이트 실패(가게 미존재)")
  @Test
  void updateStoreInformationFailedStoreNotFound() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    StoreDto.UpdateRequest request = new StoreDto.UpdateRequest();

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(1L)))
        .thenReturn(Optional.empty());

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.updateStoreInformation(entrepreneur.getId(), 1L, request);
    });

    assertEquals(STORE_NOT_FOUND, exception.getErrorCode());
  }

  @DisplayName("가게 정보 업데이트 실패(로그인한 이용자가 가게 주인이 아님)")
  @Test
  void updateStoreInformationFailedNoAuth() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(new Entrepreneur());  // 다른 사업가가 소유한 가게
    StoreDto.UpdateRequest request = new StoreDto.UpdateRequest();

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.updateStoreInformation(entrepreneur.getId(), store.getId(), request);
    });

    assertEquals(NO_AUTH_ON_STORE, exception.getErrorCode());
  }

  @DisplayName("가게 이미지 업데이트 성공")
  @Test
  void updateStoreImageSuccess() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(entrepreneur);
    StoreImage storeImage = StoreImage.builder()
        .id(1L)
        .url("http://test.com/image.jpg")
        .isRepresentative(false)
        .sequence(1)
        .store(store)
        .build();
    StoreImage anotherImage = StoreImage.builder()
        .id(2L)
        .url("http://test.com/anotherImage.jpg")
        .isRepresentative(true)
        .sequence(0)
        .store(store)
        .build();
    StoreImageDto.UpdateRequest request = new StoreImageDto.UpdateRequest(0, true);

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));
    when(imageRepository.findById(eq(storeImage.getId())))
        .thenReturn(Optional.of(storeImage));
    when(imageRepository.findAllByStoreOrderBySequenceAsc(eq(store)))
        .thenReturn(List.of(anotherImage, storeImage));

    //then
    storeService.updateStoreImage(entrepreneur.getId(), store.getId(), storeImage.getId(), request);

    assertEquals(storeImage.getSequence(), request.getSequence());
    assertEquals(anotherImage.getSequence(), 1);
    assertTrue(storeImage.getIsRepresentative());
    assertFalse(anotherImage.getIsRepresentative());
  }

  @DisplayName("가게 이미지 업데이트 실패(가게 미존재)")
  @Test
  void updateStoreImageFailedStoreNotFound() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    StoreImageDto.UpdateRequest request = new StoreImageDto.UpdateRequest();

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(1L)))
        .thenReturn(Optional.empty());

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.updateStoreImage(entrepreneur.getId(), 1L, 1L, request);
    });

    assertEquals(STORE_NOT_FOUND, exception.getErrorCode());
  }

  @DisplayName("가게 이미지 업데이트 실패(로그인한 이용자가 가게 주인이 아님)")
  @Test
  void updateStoreImageFailedNoAuth() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(new Entrepreneur());  // 다른 사업가가 소유한 가게
    StoreImage storeImage = StoreImage.builder()
        .id(1L)
        .url("http://test.com/image.jpg")
        .isRepresentative(false)
        .sequence(1)
        .store(new Store()) // 다른 store 설정
        .build();
    StoreImageDto.UpdateRequest request = new StoreImageDto.UpdateRequest();

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));
    when(imageRepository.findById(eq(storeImage.getId())))
        .thenReturn(Optional.of(storeImage));

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.updateStoreImage(entrepreneur.getId(), store.getId(), storeImage.getId(), request);
    });

    assertEquals(NO_AUTH_ON_STORE, exception.getErrorCode());
  }

  @DisplayName("가게 이미지 업데이트 실패(이미지 미존재)")
  @Test
  void updateStoreImageFailedImageNotFound() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(entrepreneur);
    StoreImageDto.UpdateRequest request = new StoreImageDto.UpdateRequest();

    //when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));
    when(imageRepository.findById(eq(1L)))
        .thenReturn(Optional.empty());

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.updateStoreImage(entrepreneur.getId(), store.getId(), 1L, request);
    });

    assertEquals(STORE_IMAGE_NOT_FOUND, exception.getErrorCode());
  }

  @DisplayName("가게 이미지 업데이트 실패(조회한 가게에 등록된 이미지가 아닐 때)")
  @Test
  void updateStoreImageFailedimageNotOnStore() {
    //given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    Store store = createRequest.toEntity(entrepreneur);
    StoreImage storeImage = StoreImage.builder()
        .id(1L)
        .url("http://test.com/image.jpg")
        .isRepresentative(false)
        .store(new Store()) // 다른 store 설정
        .build();
    StoreImageDto.UpdateRequest request = new StoreImageDto.UpdateRequest();

    // when
    when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.findByIdAndDeletedAtIsNull(eq(store.getId())))
        .thenReturn(Optional.of(store));
    when(imageRepository.findById(eq(storeImage.getId())))
        .thenReturn(Optional.of(storeImage));

    // then
    CustomException thrown = assertThrows(CustomException.class, () -> {
      storeService.updateStoreImage(entrepreneur.getId(), store.getId(), storeImage.getId(), request);
    });

    assertEquals(NO_IMAGE_ON_STORE, thrown.getErrorCode());
  }
}
