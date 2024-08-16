package com.zerobase.babdeusilbun.service;

import static com.zerobase.babdeusilbun.exception.ErrorCode.ALREADY_EXIST_STORE;
import static com.zerobase.babdeusilbun.exception.ErrorCode.ENTREPRENEUR_NOT_FOUND;
import static com.zerobase.babdeusilbun.util.ImageUtility.STORE_IMAGE_FOLDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.zerobase.babdeusilbun.component.ImageComponent;
import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.StoreImage;
import com.zerobase.babdeusilbun.dto.AddressDto;
import com.zerobase.babdeusilbun.dto.StoreDto.CreateRequest;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.repository.CategoryRepository;
import com.zerobase.babdeusilbun.repository.EntrepreneurRepository;
import com.zerobase.babdeusilbun.repository.SchoolRepository;
import com.zerobase.babdeusilbun.repository.StoreImageRepository;
import com.zerobase.babdeusilbun.repository.StoreRepository;
import com.zerobase.babdeusilbun.service.impl.StoreServiceImpl;
import com.zerobase.babdeusilbun.util.TestEntrepreneurUtility;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
  private CategoryRepository categoryRepository;

  @Mock
  private ImageComponent imageComponent;

  @InjectMocks
  private StoreServiceImpl storeService;

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
    // given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    List<MultipartFile> images = List.of();
    Store expected = createRequest.toEntity(entrepreneur);

    when(entrepreneurRepository.findByIdAndDeletedAtIsNotNull(eq(TestEntrepreneurUtility.getEntrepreneur().getId())))
        .thenReturn(Optional.of(entrepreneur));
    when(storeRepository.existsByEntrepreneurAndNameAndAddressAndDeletedAtIsNull(
        eq(entrepreneur), eq(createRequest.getName()), any()))
        .thenReturn(false);
    when(imageComponent.uploadImageList(eq(images), eq(STORE_IMAGE_FOLDER))).thenReturn(List.of("url1", "url2"));

    // when
    int uploadedImageCount = storeService.createStore(entrepreneur.getId(), images, createRequest);

    // then
    ArgumentCaptor<Store> storeCaptor = ArgumentCaptor.forClass(Store.class);
    ArgumentCaptor<List<StoreImage>> storeImagesCaptor = ArgumentCaptor.forClass(List.class);

    verify(storeRepository, times(1)).save(storeCaptor.capture());
    verify(imageRepository, times(1)).saveAll(storeImagesCaptor.capture());

    assertEquals(2, uploadedImageCount);
    List<StoreImage> savedImages = storeImagesCaptor.getValue();
    assertEquals("url1", savedImages.get(0).getUrl());
    assertTrue(savedImages.get(0).getIsRepresentative());
    assertEquals("url2", savedImages.get(1).getUrl());
    assertFalse(savedImages.get(1).getIsRepresentative());

    assertEquals(
        expected.getEntrepreneur().getId(), storeCaptor.getValue().getEntrepreneur().getId());
    assertEquals(expected.getName(), storeCaptor.getValue().getName());
    assertEquals(AddressDto.fromEntity(expected.getAddress()),
        AddressDto.fromEntity(storeCaptor.getValue().getAddress()));
  }

  @DisplayName("가게 생성 실패(중복된 가게 등록 요청)")
  @Test
  void createStoreFailedAlreadyExists() {
    // given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    List<MultipartFile> images = List.of();

    when(entrepreneurRepository.findByIdAndDeletedAtIsNotNull(eq(entrepreneur.getId()))).thenReturn(Optional.of(entrepreneur));
    when(storeRepository.existsByEntrepreneurAndNameAndAddressAndDeletedAtIsNull(
        eq(entrepreneur), eq(createRequest.getName()), any())).thenReturn(true);

    // when & then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.createStore(entrepreneur.getId(), images, createRequest);
    });

    assertEquals(ALREADY_EXIST_STORE, exception.getErrorCode());
  }

  @DisplayName("가게 생성 실패(사업가 미존재)")
  @Test
  void createStoreFailedEntrepreneurNotFound() {
    // given
    Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
    List<MultipartFile> images = List.of();

    when(entrepreneurRepository.findByIdAndDeletedAtIsNotNull(any())).thenReturn(Optional.empty());

    // when & then
    CustomException exception = assertThrows(CustomException.class, () -> {
      storeService.createStore(entrepreneur.getId(), images, createRequest);
    });

    assertEquals(ENTREPRENEUR_NOT_FOUND, exception.getErrorCode());
  }
}
