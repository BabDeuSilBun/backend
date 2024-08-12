package com.zerobase.babdeusilbun.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.zerobase.babdeusilbun.dto.SchoolDto.Information;
import com.zerobase.babdeusilbun.repository.SchoolRepository;
import com.zerobase.babdeusilbun.service.impl.SchoolServiceImpl;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@ExtendWith(MockitoExtension.class)
public class SchoolServiceTest {
  @Mock
  private SchoolRepository schoolRepository;

  @InjectMocks
  private SchoolServiceImpl schoolService;

  @DisplayName("학교 검색 서비스 테스트")
  @Test
  void searchSchoolAndCampus() {
    //given
    int page = 0;
    int size = 10;
    String search = "가짜";
    String[] keywords = search.split(" +");
    Page<Information> expectedPage = new PageImpl<>(Collections.emptyList());

    //when
    when(schoolRepository.searchSchoolNameByKeywords(keywords, page, size)).thenReturn(expectedPage);
    Page<Information> result = schoolService.searchSchoolAndCampus(page, size, search);

    //then
    assertEquals(expectedPage, result);
  }
}
