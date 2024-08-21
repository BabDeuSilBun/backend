package com.zerobase.babdeusilbun.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.babdeusilbun.dto.EntrepreneurDto;
import com.zerobase.babdeusilbun.dto.MenuDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.EntrepreneurService;
import com.zerobase.babdeusilbun.util.TestEntrepreneurUtility;
import com.zerobase.babdeusilbun.util.TestMenuUtility;
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

@WebMvcTest(EntrepreneurController.class)
public class EntrepreneurControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EntrepreneurService entrepreneurService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomUserDetails testEntrepreneur;

    private EntrepreneurDto.UpdateRequest updateRequest = EntrepreneurDto.UpdateRequest.builder().phoneNumber("010")
            .image("img").postal("p").streetAddress("s").detailAddress("d").password("pa").build();

    private final EntrepreneurDto.UpdateRequest responseUpdateRequestNotImage = EntrepreneurDto.UpdateRequest.builder().phoneNumber("010")
            .image(null).postal("p").streetAddress("s").detailAddress("d").password("pa").build();

    @BeforeEach
    void setUp() {
        //로그인 정보 세팅
        testEntrepreneur = TestEntrepreneurUtility.createTestEntrepreneur();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(testEntrepreneur, null, testEntrepreneur.getAuthorities())
        );
    }

    @DisplayName("정보 수정 컨트롤러 테스트(이미지 업로드 X, 완전 성공)")
    @Test
    void updateProfile() throws Exception {
        //given
        MockMultipartFile request = new MockMultipartFile(
                "request", "request", "application/json",
                objectMapper.writeValueAsString(updateRequest).getBytes());

        // when
        when(entrepreneurService.updateProfile(eq(testEntrepreneur.getId()), eq(null), eq(updateRequest)))
                .thenReturn(updateRequest);

        // then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/api/businesses/infos")
                        .file(request)
                        .with(csrf())
                        .with(httpRequest -> {
                            httpRequest.setMethod("PATCH");
                            return httpRequest;
                        }))
                .andExpect(status().isOk());
    }

    @DisplayName("정보 수정 컨트롤러 테스트(이미지 업로드 성공)")
    @Test
    void updateProfileWithImageUploadSuccess() throws Exception {
        //given
        MockMultipartFile request = new MockMultipartFile(
                "request", "request", "application/json",
                objectMapper.writeValueAsString(updateRequest).getBytes());

        MockMultipartFile image = new MockMultipartFile(
                "file", "image.png", "image/png", "1".getBytes());

        // when
        when(entrepreneurService.updateProfile(eq(testEntrepreneur.getId()), eq(image), eq(updateRequest)))
                .thenReturn(updateRequest);

        // then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/api/businesses/infos")
                        .file(image)
                        .file(request)
                        .with(csrf())
                        .with(httpRequest -> {
                            httpRequest.setMethod("PATCH");
                            return httpRequest;
                        }))
                .andExpect(status().isOk());
    }


    @DisplayName("정보 수정 컨트롤러 테스트(이미지 업로드 실패로 인한 부분 성공)")
    @Test
    void updateProfileWithImageUploadFail() throws Exception {
        //given
        MockMultipartFile request = new MockMultipartFile(
                "request", "request", "application/json",
                objectMapper.writeValueAsString(updateRequest).getBytes());

        MockMultipartFile image = new MockMultipartFile(
                "file", "image.png", "image/png", "1".getBytes());

        // when
        when(entrepreneurService.updateProfile(eq(testEntrepreneur.getId()), eq(image), eq(updateRequest)))
                .thenReturn(responseUpdateRequestNotImage);

        // then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/api/businesses/infos")
                        .file(image)
                        .file(request)
                        .with(csrf())
                        .with(httpRequest -> {
                            httpRequest.setMethod("PATCH");
                            return httpRequest;
                        }))
                .andExpect(status().isPartialContent());
    }
}
