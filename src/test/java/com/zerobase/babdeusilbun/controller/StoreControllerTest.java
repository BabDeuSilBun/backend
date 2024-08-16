package com.zerobase.babdeusilbun.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.babdeusilbun.dto.AddressDto;
import com.zerobase.babdeusilbun.dto.StoreDto.CreateRequest;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.StoreService;
import com.zerobase.babdeusilbun.util.TestEntrepreneurUtility;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
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
    when(storeService.createStore(eq(testEntrepreneur.getId()), eq(images), eq(createRequest)))
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
        .andDo(print())
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
    when(storeService.createStore(eq(testEntrepreneur.getId()), eq(images), eq(createRequest)))
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
        .andExpect(status().isPartialContent());
  }
}
