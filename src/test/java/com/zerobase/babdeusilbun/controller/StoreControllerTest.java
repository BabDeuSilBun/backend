package com.zerobase.babdeusilbun.controller;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.babdeusilbun.domain.Category;
import com.zerobase.babdeusilbun.dto.AddressDto;
import com.zerobase.babdeusilbun.dto.CategoryDto;
import com.zerobase.babdeusilbun.dto.CategoryDto.Information;
import com.zerobase.babdeusilbun.dto.HolidayDto;
import com.zerobase.babdeusilbun.dto.SchoolDto;
import com.zerobase.babdeusilbun.dto.StoreDto;
import com.zerobase.babdeusilbun.dto.StoreDto.CreateRequest;
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
}
