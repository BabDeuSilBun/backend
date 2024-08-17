package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.MajorDto;
import com.zerobase.babdeusilbun.repository.MajorRepository;
import com.zerobase.babdeusilbun.service.impl.MajorServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MajorServiceTest {
    @Mock
    private MajorRepository majorRepository;

    @InjectMocks
    private MajorServiceImpl majorService;

    @DisplayName("학과 검색 서비스 테스트")
    @Test
    void searchMajor() {
        //given
        int page = 0;
        int size = 10;
        String search = "가짜";
        String[] keywords = search.split(" +");
        Page<MajorDto.Information> expectedPage = new PageImpl<>(Collections.emptyList());

        //when
        when(majorRepository.searchMajorNameByKeywords(keywords, page, size)).thenReturn(expectedPage);
        Page<MajorDto.Information> result = majorService.searchMajor(search, page, size);

        //then
        assertEquals(expectedPage, result);
    }
}
