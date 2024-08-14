package com.zerobase.babdeusilbun.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.zerobase.babdeusilbun.domain.School;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.SchoolDto.Information;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.repository.SchoolRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.impl.SchoolServiceImpl;
import com.zerobase.babdeusilbun.util.TestUserUtility;
import java.util.Collections;
import java.util.Optional;
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

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private SchoolServiceImpl schoolService;

  private final CustomUserDetails testUser = TestUserUtility.createTestUser();

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
    Page<Information> result = schoolService.searchSchoolAndCampus(search, page, size);

    //then
    assertEquals(expectedPage, result);
  }

  @DisplayName("캠퍼스 검색 서비스 성공 사례 테스트")
  @Test
  void searchCampusBySchoolSuccess() {
    //given
    Long schoolId = 1L;
    Information info = new Information(1L, "가짜 학교", "가짜 캠퍼스");

    School school = School.builder()
        .id(schoolId)
        .name(info.getName())
        .campus(info.getCampus())
        .build();

    Page<Information> expectedPage = new PageImpl<>(Collections.singletonList(info));

    //when
    when(schoolRepository.findById(schoolId)).thenReturn(Optional.ofNullable(school));
    when(schoolRepository.searchCampusBySchool(info, 0, 10)).thenReturn(expectedPage);

    Page<Information> result = schoolService.searchCampusBySchool(testUser, schoolId, 0, 10);

    //then
    assertEquals(expectedPage, result);
  }

  @DisplayName("캠퍼스 검색 서비스 성공 사례 테스트(schoolId 제공되지 않은 경우)")
  @Test
  void searchCampusBySchoolWithoutSchoolIdSuccess() {
    // given
    Long schoolId = 1L;
    CustomUserDetails userDetails = TestUserUtility.createTestUser();
    User user = TestUserUtility.getUser();

    Information info = new Information(schoolId, "가짜 학교", "가짜 캠퍼스");

    School school = School.builder()
        .id(schoolId)
        .name(info.getName())
        .campus(info.getCampus())
        .build();

    Page<Information> expectedPage = new PageImpl<>(Collections.singletonList(info));

    // when
    when(userRepository.findById(userDetails.getId())).thenReturn(Optional.of(user));
    when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(school));
    when(schoolRepository.searchCampusBySchool(info, 0, 10)).thenReturn(expectedPage);

    Page<Information> result = schoolService.searchCampusBySchool(userDetails, null, 0, 10);

    // then
    assertEquals(expectedPage, result);
  }

  @DisplayName("캠퍼스 검색 서비스 실패 사례 테스트(학교 정보가 없을 때)")
  @Test
  void searchCampusBySchoolFailedWhenSchoolNotFound() {
    // given
    Long schoolId = 1L;
    CustomUserDetails user = TestUserUtility.createTestUser();

    // when
    when(schoolRepository.findById(schoolId)).thenReturn(Optional.empty());

    // then
    CustomException exception = assertThrows(CustomException.class, () -> {
      schoolService.searchCampusBySchool(user, schoolId, 0, 10);
    });

    assertEquals(ErrorCode.SCHOOL_NOT_FOUND, exception.getErrorCode());
  }

  @DisplayName("캠퍼스 검색 서비스 실패 사례 테스트(사용자 정보가 없을 때)")
  @Test
  void searchCampusBySchoolFailedWhenUserNotFound() {
    // given
    CustomUserDetails user = TestUserUtility.createTestUser();

    // when
    when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

    // then
    CustomException exception = assertThrows(CustomException.class, () -> {
      schoolService.searchCampusBySchool(user, null, 0, 10);
    });

    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
  }
}
