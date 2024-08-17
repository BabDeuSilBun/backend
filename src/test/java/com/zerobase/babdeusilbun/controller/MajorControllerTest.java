package com.zerobase.babdeusilbun.controller;

import com.zerobase.babdeusilbun.dto.MajorDto;
import com.zerobase.babdeusilbun.service.MajorService;
import com.zerobase.babdeusilbun.service.SchoolService;
import com.zerobase.babdeusilbun.util.TestUserUtility;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(MajorController.class)
public class MajorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MajorService majorService;

    @DisplayName("학과 검색 테스트")
    @WithMockUser
    @Test
    void searchMajor() throws Exception {
        //given
        MajorDto.Information info = new MajorDto.Information(1L, "가짜 학과");
        Page<MajorDto.Information> expectedPage = new PageImpl<>(List.of(info));

        //when
        when(majorService.searchMajor(anyString(), anyInt(), anyInt()))
                .thenReturn(expectedPage);

        //then
        mockMvc.perform(get("/api/majors")
                        .param("majorName", "")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("가짜 학과"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.pageable").exists());
    }
}
