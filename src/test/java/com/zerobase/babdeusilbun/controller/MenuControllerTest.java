package com.zerobase.babdeusilbun.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.babdeusilbun.dto.MenuDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.MenuService;
import com.zerobase.babdeusilbun.util.TestEntrepreneurUtility;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuController.class)
public class MenuControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomUserDetails testEntrepreneur;

    private final MenuDto.CreateRequest createRequest = MenuDto.CreateRequest.builder().
            name("가짜메뉴").description("가짜설명").image("url").price(200L).build();

    private final MenuDto.CreateRequest responseRequestNotImage = MenuDto.CreateRequest.builder().
            name("가짜메뉴").description("가짜설명").image(null).price(200L).build();

    @BeforeEach
    void setUp() {
        //로그인 정보 세팅
        testEntrepreneur = TestEntrepreneurUtility.createTestEntrepreneur();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(testEntrepreneur, null, testEntrepreneur.getAuthorities())
        );
    }

    @DisplayName("메뉴 등록 컨트롤러 테스트(이미지 업로드 X, 완전 성공)")
    @Test
    void createMenu() throws Exception {
        //given
        MockMultipartFile request = new MockMultipartFile(
                "request", "request", "application/json",
                objectMapper.writeValueAsString(createRequest).getBytes());

        // when
        when(menuService.createMenu(eq(testEntrepreneur.getId()), eq(1L), eq(null), eq(createRequest)))
                .thenReturn(responseRequestNotImage);

        // then
        mockMvc.perform(multipart(HttpMethod.POST, "/api/businesses/stores/1/menus")
                .file(request)
                .with(csrf())
                .with(httpRequest -> {
                    httpRequest.setMethod("POST");
                    return httpRequest;
                }))
                .andExpect(status().isCreated());
    }

    @DisplayName("메뉴 등록 컨트롤러 테스트(이미지 업로드 + 완전 성공)")
    @Test
    void createMenuWithImageUploadSuccess() throws Exception {
        //given
        MockMultipartFile request = new MockMultipartFile(
                "request", "request", "application/json",
                objectMapper.writeValueAsString(createRequest).getBytes());

        MockMultipartFile image = new MockMultipartFile(
                "file", "image.png", "image/png", "1".getBytes());

        // when
        when(menuService.createMenu(eq(testEntrepreneur.getId()), eq(1L), eq(image), eq(createRequest)))
                .thenReturn(createRequest);

        // then
        mockMvc.perform(multipart(HttpMethod.POST, "/api/businesses/stores/1/menus")
                        .file(image)
                        .file(request)
                        .with(csrf())
                        .with(httpRequest -> {
                            httpRequest.setMethod("POST");
                            return httpRequest;
                        }))
                .andExpect(status().isCreated());
    }

    @DisplayName("메뉴 등록 컨트롤러 테스트(이미지 업로드 실패로 인한 부분 성공)")
    @Test
    void createMenuWithImageUploadFail() throws Exception {
        //given
        MockMultipartFile request = new MockMultipartFile(
                "request", "request", "application/json",
                objectMapper.writeValueAsString(createRequest).getBytes());

        MockMultipartFile image = new MockMultipartFile(
                "file", "image.png", "image/png", "1".getBytes());

        // when
        when(menuService.createMenu(eq(testEntrepreneur.getId()), eq(1L), eq(image), eq(createRequest)))
                .thenReturn(responseRequestNotImage);

        // then
        mockMvc.perform(multipart(HttpMethod.POST, "/api/businesses/stores/1/menus")
                        .file(image)
                        .file(request)
                        .with(csrf())
                        .with(httpRequest -> {
                            httpRequest.setMethod("POST");
                            return httpRequest;
                        }))
                .andExpect(status().isPartialContent());
    }
}
