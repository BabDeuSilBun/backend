package com.zerobase.babdeusilbun.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.zerobase.babdeusilbun.dto.SchoolDto.Information;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.SchoolService;
import com.zerobase.babdeusilbun.util.TestUserUtility;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SchoolController.class)
class SchoolControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SchoolService schoolService;

  private CustomUserDetails testUser;

  @BeforeEach
  void setUp() {
    //로그인 정보 세팅
    testUser = TestUserUtility.createTestUser();

    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(testUser, null, testUser.getAuthorities())
    );
  }

  @DisplayName("학교 검색 테스트")
  @WithMockUser
  @Test
  void searchSchoolAndCampus() throws Exception {
    //given
    Information info = new Information(1L, "가짜 학교", "가짜 캠퍼스");
    Page<Information> expectedPage = new PageImpl<>(List.of(info));

    //when
    when(schoolService.searchSchoolAndCampus(anyString(), anyInt(), anyInt()))
        .thenReturn(expectedPage);

    //then
    mockMvc.perform(get("/api/schools")
            .param("schoolName", "")
            .param("page", "0")
            .param("size", "10")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content[0].name").value("가짜 학교"))
        .andExpect(jsonPath("$.totalElements").value(1))
        .andExpect(jsonPath("$.pageable").exists());
  }

  @DisplayName("캠퍼스 검색 테스트(schoolId에 값을 입력했을 경우)")
  @Test
  void searchCampusBySchoolWithSchoolId() throws Exception {
    //given
    Information info = new Information(2L, "가짜 학교", "가짜 캠퍼스");
    Page<Information> expectedPage = new PageImpl<>(List.of(info));

    //when
    when(schoolService.searchCampusBySchool(testUser, 2L, 0, 10))
        .thenReturn(expectedPage);

    //then
    mockMvc.perform(get("/api/campus")
            .param("schoolId", "2")
            .param("page", "0")
            .param("size", "10")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content[0].name").value("가짜 학교"))
        .andExpect(jsonPath("$.totalElements").value(1))
        .andExpect(jsonPath("$.pageable").exists());
  }

  @DisplayName("캠퍼스 검색 테스트(schoolId에 값이 없을 경우)")
  @Test
  void searchCampusBySchoolWithUserInfo() throws Exception {
    //given
    Information info = new Information(1L, "가짜 학교", "가짜 캠퍼스");
    Page<Information> expectedPage = new PageImpl<>(List.of(info));

    //when
    when(schoolService.searchCampusBySchool(testUser, null, 0, 10))
        .thenReturn(expectedPage);

    //then
    mockMvc.perform(get("/api/campus")
            .param("schoolId", "")
            .param("page", "0")
            .param("size", "10")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content[0].name").value("가짜 학교"))
        .andExpect(jsonPath("$.totalElements").value(1))
        .andExpect(jsonPath("$.pageable").exists());
  }
}
