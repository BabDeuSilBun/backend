package com.zerobase.babdeusilbun.util;

import com.zerobase.babdeusilbun.domain.School;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.security.dto.UserCustomUserDetails;

public class TestUserUtility {
  public static UserCustomUserDetails createTestUser() {
    School school = School.builder()
        .id(1L)
        .name("가짜 학교")
        .campus("가짜캠퍼스")
        .build();

    User user = User.builder()
        .id(1L)
        .email("test@test.com")
        .password("password")
        .school(school)
        .build();

    return new UserCustomUserDetails(user);
  }
}
