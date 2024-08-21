package com.zerobase.babdeusilbun.controller;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.babdeusilbun.domain.Category;
import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.dto.AddressDto;
import com.zerobase.babdeusilbun.dto.CategoryDto;
import com.zerobase.babdeusilbun.dto.CategoryDto.Information;
import com.zerobase.babdeusilbun.dto.EntrepreneurDto;
import com.zerobase.babdeusilbun.dto.HolidayDto;
import com.zerobase.babdeusilbun.dto.MenuDto;
import com.zerobase.babdeusilbun.dto.SchoolDto;
import com.zerobase.babdeusilbun.dto.StoreCategoryDto;
import com.zerobase.babdeusilbun.dto.StoreDto;
import com.zerobase.babdeusilbun.dto.StoreDto.CreateRequest;
import com.zerobase.babdeusilbun.dto.StoreImageDto;
import com.zerobase.babdeusilbun.dto.StoreImageDto.Thumbnail;
import com.zerobase.babdeusilbun.dto.StoreSchoolDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.StoreService;
import com.zerobase.babdeusilbun.util.TestEntrepreneurUtility;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(StoreController.class)
public class StoreControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StoreService storeService;

  @Autowired
  private ObjectMapper objectMapper;

  private CustomUserDetails testEntrepreneur;

  private final List<Category> categories = List.of(
      Category.builder().id(1L).name("야식").build(),
      Category.builder().id(2L).name("점심").build(),
      Category.builder().id(3L).name("저녁").build()
  );

  private final CreateRequest createRequest = CreateRequest.builder()
      .name("Test Store")
      .description("A test store")
      .minPurchasePrice(10000L)
      .minDeliveryTime(30)
      .maxDeliveryTime(60)
      .deliveryPrice(2500L)
      .address(new AddressDto("00000", "도로명주소", "상세주소"))
      .phoneNumber("01012345678")
      .openTime(LocalTime.of(9, 0))
      .closeTime(LocalTime.of(21, 0))
      .build();

  @BeforeEach
  void setUp() {
    //로그인 정보 세팅
    testEntrepreneur = TestEntrepreneurUtility.createTestEntrepreneur();

    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(testEntrepreneur, null, testEntrepreneur.getAuthorities())
    );
  }

  @DisplayName("상점 등록 컨트롤러 테스트(완전 성공)")
  @Test
  void createStoreSuccess() throws Exception {
    //given
    MockMultipartFile request = new MockMultipartFile(
        "request", "request", "application/json",
        objectMapper.writeValueAsString(createRequest).getBytes());

    MockMultipartFile image1 = new MockMultipartFile(
        "files", "image1.png", "image/png", "1".getBytes());

    MockMultipartFile image2 = new MockMultipartFile(
        "files", "image2.png", "image/png", "2".getBytes());

    List<MultipartFile> images = List.of(image1, image2);

    //when
    when(storeService.createStore(eq(testEntrepreneur.getId()), eq(createRequest)))
        .thenReturn(StoreDto.IdResponse.builder().storeId(1L).build());

    when(storeService.uploadImageToStore(eq(testEntrepreneur.getId()), eq(images), eq(1L)))
        .thenReturn(2);

    //then
    mockMvc.perform(multipart(HttpMethod.POST, "/api/businesses/stores")
            .file(image1)
            .file(image2)
            .file(request)
            .with(csrf())
            .with(httpRequest -> {
              httpRequest.setMethod("POST");
              return httpRequest;
            }))
        .andExpect(jsonPath("$.storeId").value(1L))
        .andExpect(status().isCreated());
  }

  @DisplayName("상점 등록 컨트롤러 테스트(부분 성공)")
  @Test
  void createStorePartialSuccess() throws Exception {
    //given


    MockMultipartFile request = new MockMultipartFile(
        "request", "request", "application/json",
        objectMapper.writeValueAsString(createRequest).getBytes());

    MockMultipartFile image1 = new MockMultipartFile(
        "files", "image1.png", "image/png", "1".getBytes());

    MockMultipartFile image2 = new MockMultipartFile(
        "files", "image2.png", "image/png", "2".getBytes());

    List<MultipartFile> images = List.of(image1, image2);

    //when
    when(storeService.createStore(eq(testEntrepreneur.getId()), eq(createRequest)))
        .thenReturn(StoreDto.IdResponse.builder().storeId(1L).build());

    when(storeService.uploadImageToStore(eq(testEntrepreneur.getId()), eq(images), eq(1L)))
        .thenReturn(1);

    //then
    mockMvc.perform(multipart("/api/businesses/stores")
            .file(request)
            .file(image1)
            .file(image2)
            .with(csrf())
            .with(httpRequest -> {
              httpRequest.setMethod("POST");
              return httpRequest;
            }))
        .andExpect(jsonPath("$.storeId").value(1L))
        .andExpect(status().isPartialContent());
  }

  @DisplayName("카테고리 조회 컨트롤러 테스트")
  @Test
  void getAllCategoriesSuccess() throws Exception {
    Pageable pageable = PageRequest.of(0, 10);
    List<CategoryDto.Information> informationList = categories.stream().map(Information::fromEntity).toList();

    Page<Information> categoryPage = new PageImpl<>(informationList, pageable, categories.size());

    when(storeService.getAllCategories(eq(pageable.getPageNumber()), eq(pageable.getPageSize())))
        .thenReturn(categoryPage);

    mockMvc.perform(get("/api/stores/categories")
            .param("page", String.valueOf(pageable.getPageNumber()))
            .param("size", String.valueOf(pageable.getPageSize()))
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()));
  }

  @DisplayName("상점에 카테고리 등록 컨트롤러 테스트(성공)")
  @Test
  void enrollToCategorySuccess() throws Exception {
    CategoryDto.IdsRequest request = new CategoryDto.IdsRequest(Set.of(1L, 2L));

    when(storeService.enrollToCategory(eq(testEntrepreneur.getId()), eq(1L), eq(request)))
        .thenReturn(2);

    mockMvc.perform(post("/api/businesses/stores/1/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isOk());
  }

  @DisplayName("상점에 카테고리 삭제 컨트롤러 테스트(성공)")
  @Test
  void deleteOnCategorySuccess() throws Exception {
    CategoryDto.IdsRequest request = new CategoryDto.IdsRequest(Set.of(1L, 2L));

    when(storeService.deleteOnCategory(eq(testEntrepreneur.getId()), eq(1L), eq(request)))
        .thenReturn(2);

    mockMvc.perform(delete("/api/businesses/stores/1/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isOk());
  }

  @DisplayName("상점에 카테고리 등록 컨트롤러 테스트(부분 성공)")
  @Test
  void enrollToCategoryPartialSuccess() throws Exception {
    CategoryDto.IdsRequest request = new CategoryDto.IdsRequest(Set.of(1L, 2L));

    when(storeService.enrollToCategory(eq(testEntrepreneur.getId()), eq(1L), eq(request)))
        .thenReturn(1);

    mockMvc.perform(post("/api/businesses/stores/1/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isPartialContent());
  }

  @DisplayName("상점에 카테고리 삭제 컨트롤러 테스트(부분 성공)")
  @Test
  void deleteOnCategoryPartialSuccess() throws Exception {
    CategoryDto.IdsRequest request = new CategoryDto.IdsRequest(Set.of(1L, 2L));

    when(storeService.deleteOnCategory(eq(testEntrepreneur.getId()), eq(1L), eq(request)))
        .thenReturn(1);

    mockMvc.perform(delete("/api/businesses/stores/1/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isPartialContent());
  }

  @DisplayName("상점에 카테고리 등록 컨트롤러 테스트(변동 없음)")
  @Test
  void enrollToCategoryNoContent() throws Exception {
    CategoryDto.IdsRequest request = new CategoryDto.IdsRequest(Set.of());

    when(storeService.enrollToCategory(eq(testEntrepreneur.getId()), eq(1L), eq(request)))
        .thenReturn(0);

    mockMvc.perform(post("/api/businesses/stores/1/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isNotModified());
  }

  @DisplayName("상점에 카테고리 삭제 컨트롤러 테스트(변동 없음)")
  @Test
  void deleteOnCategoryNoContent() throws Exception {
    CategoryDto.IdsRequest request = new CategoryDto.IdsRequest(Set.of());

    when(storeService.deleteOnCategory(eq(testEntrepreneur.getId()), eq(1L), eq(request)))
        .thenReturn(0);

    mockMvc.perform(delete("/api/businesses/stores/1/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isNotModified());
  }

  @DisplayName("상점에 캠퍼스 등록 컨트롤러 테스트(성공)")
  @Test
  void enrollToCampusSuccess() throws Exception {
    SchoolDto.IdsRequest request = new SchoolDto.IdsRequest(Set.of(1L, 2L));

    when(storeService.enrollSchoolsToStore(eq(testEntrepreneur.getId()), eq(1L), eq(request)))
        .thenReturn(2);

    mockMvc.perform(post("/api/businesses/stores/1/schools")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isOk());
  }

  @DisplayName("상점에 캠퍼스 제거 컨트롤러 테스트(성공)")
  @Test
  void deleteOnCampusSuccess() throws Exception {
    SchoolDto.IdsRequest request = new SchoolDto.IdsRequest(Set.of(1L, 2L));

    when(storeService.deleteSchoolsOnStore(eq(testEntrepreneur.getId()), eq(1L), eq(request)))
        .thenReturn(2);

    mockMvc.perform(delete("/api/businesses/stores/1/schools")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isOk());
  }

  @DisplayName("상점에 캠퍼스 등록 컨트롤러 테스트(부분 성공)")
  @Test
  void enrollToCampusPartialSuccess() throws Exception {
    SchoolDto.IdsRequest request = new SchoolDto.IdsRequest(Set.of(1L, 2L));

    when(storeService.enrollSchoolsToStore(eq(testEntrepreneur.getId()), eq(1L), eq(request)))
        .thenReturn(1);

    mockMvc.perform(post("/api/businesses/stores/1/schools")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isPartialContent());
  }

  @DisplayName("상점에 캠퍼스 제거 컨트롤러 테스트(부분 성공)")
  @Test
  void deleteOnCampusPartialSuccess() throws Exception {
    SchoolDto.IdsRequest request = new SchoolDto.IdsRequest(Set.of(1L, 2L));

    when(storeService.deleteSchoolsOnStore(eq(testEntrepreneur.getId()), eq(1L), eq(request)))
        .thenReturn(1);

    mockMvc.perform(delete("/api/businesses/stores/1/schools")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isPartialContent());
  }

  @DisplayName("상점에 캠퍼스 등록 컨트롤러 테스트(변동 없음)")
  @Test
  void enrollToCampusNoChanges() throws Exception {
    SchoolDto.IdsRequest request = new SchoolDto.IdsRequest(Set.of(1L, 2L));

    when(storeService.enrollSchoolsToStore(eq(testEntrepreneur.getId()), eq(1L), eq(request)))
        .thenReturn(0);

    mockMvc.perform(post("/api/businesses/stores/1/schools")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isNotModified());
  }

  @DisplayName("상점에 캠퍼스 제거 컨트롤러 테스트(변동 없음)")
  @Test
  void deleteOnCampusNoChanges() throws Exception {
    SchoolDto.IdsRequest request = new SchoolDto.IdsRequest(Set.of(1L, 2L));

    when(storeService.deleteSchoolsOnStore(eq(testEntrepreneur.getId()), eq(1L), eq(request)))
        .thenReturn(0);

    mockMvc.perform(delete("/api/businesses/stores/1/schools")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isNotModified());
  }

  @DisplayName("상점에 휴무일 등록 컨트롤러 테스트(성공)")
  @Test
  void enrollToHolidaySuccess() throws Exception {
    HolidayDto.HolidaysRequest request = new HolidayDto.HolidaysRequest(Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));

    when(storeService.enrollHolidaysToStore(eq(testEntrepreneur.getId()), eq(1L), eq(request)))
        .thenReturn(2);

    mockMvc.perform(post("/api/businesses/stores/1/holidays")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isOk());
  }

  @DisplayName("상점에 휴무일 삭제 컨트롤러 테스트(성공)")
  @Test
  void deleteOnHolidaySuccess() throws Exception {
    HolidayDto.HolidaysRequest request = new HolidayDto.HolidaysRequest(Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));

    when(storeService.deleteHolidaysOnStore(eq(testEntrepreneur.getId()), eq(1L), eq(request)))
        .thenReturn(2);

    mockMvc.perform(delete("/api/businesses/stores/1/holidays")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isOk());
  }

  @DisplayName("상점에 휴무일 등록 컨트롤러 테스트(부분 성공)")
  @Test
  void enrollToHolidayPartialSuccess() throws Exception {
    HolidayDto.HolidaysRequest request = new HolidayDto.HolidaysRequest(Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));

    when(storeService.enrollHolidaysToStore(eq(testEntrepreneur.getId()), eq(1L), eq(request)))
        .thenReturn(1);

    mockMvc.perform(post("/api/businesses/stores/1/holidays")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isPartialContent());
  }

  @DisplayName("상점에 휴무일 삭제 컨트롤러 테스트(부분 성공)")
  @Test
  void deleteOnHolidayPartialSuccess() throws Exception {
    HolidayDto.HolidaysRequest request = new HolidayDto.HolidaysRequest(Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));

    when(storeService.deleteHolidaysOnStore(eq(testEntrepreneur.getId()), eq(1L), eq(request)))
        .thenReturn(1);

    mockMvc.perform(delete("/api/businesses/stores/1/holidays")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isPartialContent());
  }

  @DisplayName("상점에 휴무일 등록 컨트롤러 테스트(변동 없음)")
  @Test
  void enrollToHolidayNoChanges() throws Exception {
    HolidayDto.HolidaysRequest request = new HolidayDto.HolidaysRequest(Set.of());

    when(storeService.enrollHolidaysToStore(eq(testEntrepreneur.getId()), eq(1L), eq(request)))
        .thenReturn(0);

    mockMvc.perform(post("/api/businesses/stores/1/holidays")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isNotModified());
  }

  @DisplayName("상점에 휴무일 삭제 컨트롤러 테스트(변동 없음)")
  @Test
  void deleteOnHolidayNoChanges() throws Exception {
    HolidayDto.HolidaysRequest request = new HolidayDto.HolidaysRequest(Set.of());

    when(storeService.deleteHolidaysOnStore(eq(testEntrepreneur.getId()), eq(1L), eq(request)))
        .thenReturn(0);

    mockMvc.perform(delete("/api/businesses/stores/1/holidays")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isNotModified());
  }

  @DisplayName("상점 이미지 등록 컨트롤러 테스트(성공)")
  @Test
  void enrollImagesToStoreSuccess() throws Exception {
    MockMultipartFile image1 = new MockMultipartFile(
        "files", "image1.png", "image/png", "1".getBytes());
    MockMultipartFile image2 = new MockMultipartFile(
        "files", "image2.png", "image/png", "2".getBytes());

    List<MultipartFile> images = List.of(image1, image2);

    when(storeService.uploadImageToStore(eq(testEntrepreneur.getId()), eq(images), eq(1L)))
        .thenReturn(2);

    mockMvc.perform(multipart("/api/businesses/stores/1/images")
            .file(image1)
            .file(image2)
            .with(csrf()))
        .andExpect(status().isOk());
  }

  @DisplayName("상점 이미지 등록 컨트롤러 테스트(변동 없음)")
  @Test
  void enrollImagesToStoreNoContent() throws Exception {
    MockMultipartFile image1 = new MockMultipartFile(
        "files", "image1.png", "image/png", "1".getBytes());

    List<MultipartFile> images = List.of(image1);

    when(storeService.uploadImageToStore(eq(testEntrepreneur.getId()), eq(images), eq(1L)))
        .thenReturn(0);

    mockMvc.perform(multipart("/api/businesses/stores/1/images")
            .file(image1)
            .with(csrf()))
        .andExpect(status().isNotModified());
  }

  @DisplayName("상점 이미지 등록 컨트롤러 테스트(부분 성공)")
  @Test
  void enrollImagesToStorePartialSuccess() throws Exception {
    MockMultipartFile image1 = new MockMultipartFile(
        "files", "image1.png", "image/png", "1".getBytes());
    MockMultipartFile image2 = new MockMultipartFile(
        "files", "image2.png", "image/png", "2".getBytes());

    List<MultipartFile> images = List.of(image1, image2);

    when(storeService.uploadImageToStore(eq(testEntrepreneur.getId()), eq(images), eq(1L)))
        .thenReturn(1);

    mockMvc.perform(multipart("/api/businesses/stores/1/images")
            .file(image1)
            .file(image2)
            .with(csrf()))
        .andExpect(status().isPartialContent());
  }

  @DisplayName("상점 이미지 삭제 컨트롤러 테스트(성공)")
  @Test
  void deleteImageOnStoreSuccess() throws Exception {
    when(storeService.deleteImageOnStore(eq(testEntrepreneur.getId()), eq(1L), anyLong()))
        .thenReturn(true);

    mockMvc.perform(delete("/api/businesses/stores/1/images/1")
            .with(csrf()))
        .andExpect(status().isNoContent());
  }

  @DisplayName("상점 이미지 삭제 컨트롤러 테스트(부분 성공: DB 삭제 성공, 버킷 삭제 실패)")
  @Test
  void deleteImageOnStorePartialSuccess() throws Exception {
    when(storeService.deleteImageOnStore(eq(testEntrepreneur.getId()), eq(1L), eq(1L)))
        .thenReturn(false);

    mockMvc.perform(delete("/api/businesses/stores/1/images/1")
            .with(csrf()))
        .andExpect(status().isPartialContent());
  }

  @DisplayName("상점 이미지 설정 변경 컨트롤러 테스트")
  @Test
  void updateStoreImageSuccess() throws Exception {
    StoreImageDto.UpdateRequest request = StoreImageDto.UpdateRequest.builder()
        .sequence(2)
        .isRepresentative(true)
        .build();

    doNothing().when(storeService).updateStoreImage(eq(testEntrepreneur.getId()), eq(1L), eq(1L), eq(request));

    mockMvc.perform(patch("/api/businesses/stores/1/images/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isOk());
  }

  @DisplayName("상점 정보 수정 컨트롤러 테스트")
  @Test
  void updateStoreInformationSuccess() throws Exception {
    StoreDto.UpdateRequest request = StoreDto.UpdateRequest.builder().build();

    doNothing().when(storeService).updateStoreInformation(eq(testEntrepreneur.getId()), eq(1L), eq(request));

    mockMvc.perform(patch("/api/businesses/stores/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isOk());
  }

  @DisplayName("상점 삭제 컨트롤러 테스트")
  @Test
  void deleteStoreSuccess() throws Exception {
    doNothing().when(storeService).deleteStore(eq(testEntrepreneur.getId()), eq(1L));

    mockMvc.perform(delete("/api/businesses/stores/1")
            .with(csrf()))
        .andExpect(status().isNoContent());
  }

  @DisplayName("등록한 상점 리스트 조회 컨트롤러 테스트")
  @Test
  void getAllStoresByEntrepreneurSuccess() throws Exception {
    List<StoreDto.SimpleInformation> storeList = List.of(
        StoreDto.SimpleInformation.builder().storeId(1L).name("가나다 상점").build(),
        StoreDto.SimpleInformation.builder().storeId(2L).name("나다라 상점").build()
    );
    Page<StoreDto.SimpleInformation> storePage = new PageImpl<>(storeList);

    when(storeService.getAllStoresByEntrepreneur(
        eq(testEntrepreneur.getId()), eq(0), eq(10), eq(false)))
        .thenReturn(storePage);

    mockMvc.perform(get("/api/businesses/stores")
            .param("page", "0")
            .param("size", "10")
            .param("unprocessedOnly", "false")
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(jsonPath("$.content[0].storeId").value(1L))
        .andExpect(jsonPath("$.content[0].name").value("가나다 상점"));
  }

  @DisplayName("상점 정보 조회 컨트롤러 테스트")
  @Test
  void getStoreInfoSuccess() throws Exception {
    Long storeId = 1L;
    StoreDto.PrincipalInformation storeInfo = StoreDto.PrincipalInformation.fromEntity(
        Store.builder()
            .name("반환 상점")
            .entrepreneur(Entrepreneur.builder()
                .id(testEntrepreneur.getId())
                .build())
            .build());

    when(storeService.getStore(eq(storeId))).thenReturn(storeInfo);

    mockMvc.perform(get("/api/stores/{storeId}", storeId)
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()));
  }

  @DisplayName("상점별 휴무일 조회 컨트롤러 테스트")
  @Test
  void getAllHolidaysSuccess() throws Exception {
    //given
    Long storeId = 1L;
    Pageable pageable = PageRequest.of(0, 10);

    HolidayDto.Information holiday1 = new HolidayDto.Information() {
      @Override
      public Long getHolidayId() {
        return 1L;
      }

      @Override
      public String getDayOfWeek() {
        return "MONDAY";
      }
    };

    HolidayDto.Information holiday2 = new HolidayDto.Information() {
      @Override
      public Long getHolidayId() {
        return 2L;
      }

      @Override
      public String getDayOfWeek() {
        return "TUESDAY";
      }
    };

    //when

    List<HolidayDto.Information> holidays = List.of(holiday1, holiday2);

    Page<HolidayDto.Information> holidayPage = new PageImpl<>(holidays, pageable, holidays.size());

    when(storeService.getAllHolidays(eq(storeId), eq(pageable.getPageNumber()), eq(pageable.getPageSize())))
        .thenReturn(holidayPage);

    //then
    mockMvc.perform(get("/api/stores/{storeId}/holidays", storeId)
            .param("page", String.valueOf(pageable.getPageNumber()))
            .param("size", String.valueOf(pageable.getPageSize()))
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()));
  }

  @DisplayName("상점별 카테고리 조회 컨트롤러 테스트")
  @Test
  void getAllCategoriesByStoreSuccess() throws Exception {
    Long storeId = 1L;
    Pageable pageable = PageRequest.of(0, 10);

    StoreCategoryDto.Information category1 = new StoreCategoryDto.Information() {
      @Override
      public Long getCategoryId() {
        return 1L;
      }

      @Override
      public String getName() {
        return "고기";
      }
    };

    StoreCategoryDto.Information category2 = new StoreCategoryDto.Information() {
      @Override
      public Long getCategoryId() {
        return 2L;
      }

      @Override
      public String getName() {
        return "카페";
      }
    };

    List<StoreCategoryDto.Information> categories = List.of(category1, category2);
    Page<StoreCategoryDto.Information> categoryPage = new PageImpl<>(categories, pageable, categories.size());

    when(storeService.getAllCategories(eq(storeId), eq(pageable.getPageNumber()), eq(pageable.getPageSize())))
        .thenReturn(categoryPage);

    mockMvc.perform(get("/api/stores/{storeId}/categories", storeId)
            .param("page", String.valueOf(pageable.getPageNumber()))
            .param("size", String.valueOf(pageable.getPageSize()))
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()));
  }

  @DisplayName("가게별 메뉴 리스트 조회 컨트롤러 테스트")
  @Test
  void getAllMenusSuccess() throws Exception {
    Long storeId = 1L;
    Pageable pageable = PageRequest.of(0, 10);

    MenuDto.Information menu1 = new MenuDto.Information() {
      @Override
      public Long getMenuId() {
        return 1L;
      }

      @Override
      public String getName() {
        return "간판 메뉴1";
      }

      @Override
      public String getImage() {
        return "http://~~~";
      }

      @Override
      public String getDescription() {
        return "메뉴 설명1";
      }

      @Override
      public Long getPrice() {
        return 1000L;
      }
    };

    MenuDto.Information menu2 = new MenuDto.Information() {
      @Override
      public Long getMenuId() {
        return 2L;
      }

      @Override
      public String getName() {
        return "그냥 메뉴2";
      }

      @Override
      public String getImage() {
        return "http://~~~~";
      }

      @Override
      public String getDescription() {
        return "간단 설명2";
      }

      @Override
      public Long getPrice() {
        return 2000L;
      }
    };

    List<MenuDto.Information> menus = List.of(menu1, menu2);
    Page<MenuDto.Information> menuPage = new PageImpl<>(menus, pageable, menus.size());

    when(storeService.getAllMenus(eq(storeId), eq(pageable.getPageNumber()), eq(pageable.getPageSize())))
        .thenReturn(menuPage);

    mockMvc.perform(get("/api/stores/{storeId}/menus", storeId)
            .param("page", String.valueOf(pageable.getPageNumber()))
            .param("size", String.valueOf(pageable.getPageSize()))
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()));
  }

  @DisplayName("상점별 사업자 정보 조회 컨트롤러 테스트")
  @Test
  void getEntrepreneurSuccess() throws Exception {
    Long storeId = 1L;
    EntrepreneurDto.SimpleInformation entrepreneurInfo = EntrepreneurDto.SimpleInformation
        .fromEntity(Entrepreneur.builder()
            .id(1L)
            .businessNumber("사업가")
            .build());

    when(storeService.getEntrepreneur(eq(storeId))).thenReturn(entrepreneurInfo);

    mockMvc.perform(get("/api/stores/{storeId}/entrepreneur", storeId)
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()));
  }

  @DisplayName("상점별 배달가능 캠퍼스 조회 컨트롤러 테스트")
  @Test
  void getAllSchoolsSuccess() throws Exception {
    Long storeId = 1L;
    Pageable pageable = PageRequest.of(0, 10);

    StoreSchoolDto.Information school1 = new StoreSchoolDto.Information() {
      @Override
      public Long getSchoolId() {
        return 1L;
      }

      @Override
      public String getName() {
        return "ㅇㅇ대학교";
      }

      @Override
      public String getCampus() {
        return "ㅇㅇ캠퍼스";
      }
    };

    StoreSchoolDto.Information school2 = new StoreSchoolDto.Information() {
      @Override
      public Long getSchoolId() {
        return 2L;
      }

      @Override
      public String getName() {
        return "ㅁㅁ대학교";
      }

      @Override
      public String getCampus() {
        return "ㅁㅁ캠퍼스";
      }
    };

    List<StoreSchoolDto.Information> schools = List.of(school1, school2);
    Page<StoreSchoolDto.Information> schoolPage = new PageImpl<>(schools, pageable, schools.size());

    when(storeService.getAllSchools(eq(storeId), eq(pageable.getPageNumber()), eq(pageable.getPageSize())))
        .thenReturn(schoolPage);

    mockMvc.perform(get("/api/stores/{storeId}/schools", storeId)
            .param("page", String.valueOf(pageable.getPageNumber()))
            .param("size", String.valueOf(pageable.getPageSize()))
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()));
  }

  @DisplayName("상점 이미지 전체 조회 컨트롤러 테스트")
  @Test
  void getAllImagesSuccess() throws Exception {
    Long storeId = 1L;
    Pageable pageable = PageRequest.of(0, 10);

    StoreImageDto.Information image1 = new StoreImageDto.Information() {
      @Override
      public Long getImageId() {
        return 1L;
      }

      @Override
      public String getUrl() {
        return "http://www.~";
      }

      @Override
      public int getSequence() {
        return 0;
      }

      @Override
      public boolean getIsRepresentative() {
        return false;
      }
    };

    StoreImageDto.Information image2 = new StoreImageDto.Information() {
      @Override
      public Long getImageId() {
        return 2L;
      }

      @Override
      public String getUrl() {
        return "http://222.~~~";
      }

      @Override
      public int getSequence() {
        return 1;
      }

      @Override
      public boolean getIsRepresentative() {
        return true;
      }
    };

    List<StoreImageDto.Information> images = List.of(image1, image2);
    Page<StoreImageDto.Information> imagePage = new PageImpl<>(images, pageable, images.size());

    when(storeService.getAllImages(eq(storeId), eq(pageable.getPageNumber()), eq(pageable.getPageSize())))
        .thenReturn(imagePage);

    mockMvc.perform(get("/api/stores/{storeId}/images", storeId)
            .param("page", String.valueOf(pageable.getPageNumber()))
            .param("size", String.valueOf(pageable.getPageSize()))
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()));
  }

  @DisplayName("썸네일 조회 컨트롤러 테스트")
  @Test
  void getThumbnailSuccess() throws Exception {
    Long storeId = 1L;
    StoreImageDto.Thumbnail thumbnail = new Thumbnail() {
      @Override
      public Long getImageId() {
        return 1L;
      }

      @Override
      public String getUrl() {
        return "http://~~";
      }
    };

    when(storeService.getThumbnail(eq(storeId))).thenReturn(thumbnail);

    mockMvc.perform(get("/api/stores/{storeId}/thumbnail", storeId)
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()));
  }
}
