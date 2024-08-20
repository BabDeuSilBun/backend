package com.zerobase.babdeusilbun.service;

import static com.zerobase.babdeusilbun.security.type.Role.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.zerobase.babdeusilbun.component.ImageComponent;
import com.zerobase.babdeusilbun.domain.*;
import com.zerobase.babdeusilbun.dto.UserDto.UpdateAccount;
import com.zerobase.babdeusilbun.dto.UserDto.UpdateAddress;
import com.zerobase.babdeusilbun.dto.UserDto.UpdateRequest;
import com.zerobase.babdeusilbun.enums.Bank;
import com.zerobase.babdeusilbun.repository.MajorRepository;
import com.zerobase.babdeusilbun.repository.SchoolRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.impl.UserServiceImpl;
import com.zerobase.babdeusilbun.util.TestUserUtility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

    when(userRepository.findByIdAndDeletedAtIsNull(eq(user.getId()))).thenReturn(java.util.Optional.of(user));
    when(schoolRepository.findById(eq(request.getSchoolId()))).thenReturn(java.util.Optional.of(school));
    when(majorRepository.findById(eq(request.getMajorId()))).thenReturn(java.util.Optional.of(major));
    when(passwordEncoder.encode(eq(request.getPassword()))).thenReturn("encodedPassword");

    // when
    UpdateRequest updatedRequest = userService.updateProfile(1L, image, request);

    // then
    assertEquals(request.getNickname(), user.getNickname());
    assertEquals("encodedPassword", user.getPassword());
    assertEquals(originImage, user.getImage());
  }

  @DisplayName("내 주소 수정 테스트")
  @Test
  void updateAddress() {
    // given
    User user = TestUserUtility.getUser();
    UpdateAddress updateAddress = new UpdateAddress("변경주소1", "변경주소2", "변경주소3");

    when(userRepository.findByIdAndDeletedAtIsNull(eq(user.getId()))).thenReturn(java.util.Optional.of(user));

    // when
    UpdateAddress address = userService.updateAddress(user.getId(), updateAddress);

    // then
    assertEquals(address.getPostal(), user.getAddress().getPostal());
    assertEquals(address.getStreetAddress(), user.getAddress().getStreetAddress());
    assertEquals(address.getDetailAddress(), user.getAddress().getDetailAddress());
  }

  @DisplayName("내 계좌 수정 테스트")
  @Test
  void updateAccount() {
    // given
    User user = TestUserUtility.getUser();
    UpdateAccount updateAccount = new UpdateAccount(Bank.HANA, "계좌번호", "계좌주인");

    // when
    when(userRepository.findByIdAndDeletedAtIsNull(eq(user.getId()))).thenReturn(java.util.Optional.of(user));
    BankAccount returnAccount = userService.updateAccount(user.getId(), updateAccount);

    // then
    assertEquals(updateAccount.getBankName(), returnAccount.getBank());
    assertEquals(updateAccount.getAccountOwner(), returnAccount.getAccountOwner());
    assertEquals(updateAccount.getAccountNumber(), returnAccount.getAccountNumber());
  }
}
