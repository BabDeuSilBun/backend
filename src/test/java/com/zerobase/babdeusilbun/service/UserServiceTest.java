package com.zerobase.babdeusilbun.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.zerobase.babdeusilbun.component.ImageComponent;
import com.zerobase.babdeusilbun.domain.Major;
import com.zerobase.babdeusilbun.domain.School;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.UserDto.UpdateRequest;
import com.zerobase.babdeusilbun.repository.MajorRepository;
import com.zerobase.babdeusilbun.repository.SchoolRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.service.impl.UserServiceImpl;
import com.zerobase.babdeusilbun.util.TestUserUtility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
  @Mock
  private UserRepository userRepository;

  @Mock
  private SchoolRepository schoolRepository;

  @Mock
  private MajorRepository majorRepository;

  @Mock
  private ImageComponent imageComponent;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserServiceImpl userService;

  @DisplayName("내 정보 수정 테스트")
  @Test
  void updateProfile() {
    // given
    User user = TestUserUtility.getUser();
    UpdateRequest request = new UpdateRequest(
        "newNickname", "newPassword", null, null, 1L, 1L);
    School school = new School();
    Major major = new Major();
    MultipartFile image = null;

    String originImage = user.getImage();

    when(userRepository.findById(eq(user.getId()))).thenReturn(java.util.Optional.of(user));
    when(schoolRepository.findById(eq(request.getSchoolId()))).thenReturn(java.util.Optional.of(school));
    when(majorRepository.findById(eq(request.getMajorId()))).thenReturn(java.util.Optional.of(major));
    when(passwordEncoder.encode(eq(request.getPassword()))).thenReturn("encodedPassword");

    // when
    UpdateRequest updatedRequest = userService.updateProfile(1L, image, request);

    // then
    assertEquals("newNickname", user.getNickname());
    assertEquals("encodedPassword", user.getPassword());
    assertEquals(originImage, user.getImage());
  }
}
