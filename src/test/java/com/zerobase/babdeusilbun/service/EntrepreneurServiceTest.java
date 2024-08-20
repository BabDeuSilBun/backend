package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.component.ImageComponent;
import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.Major;
import com.zerobase.babdeusilbun.domain.School;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.EntrepreneurDto;
import com.zerobase.babdeusilbun.dto.UserDto;
import com.zerobase.babdeusilbun.repository.EntrepreneurRepository;
import com.zerobase.babdeusilbun.service.impl.EntrepreneurServiceImpl;
import com.zerobase.babdeusilbun.util.TestEntrepreneurUtility;
import com.zerobase.babdeusilbun.util.TestUserUtility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.zerobase.babdeusilbun.util.ImageUtility.ENTREPRENEUR_IMAGE_FOLDER;
import static com.zerobase.babdeusilbun.util.ImageUtility.MENU_IMAGE_FOLDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EntrepreneurServiceTest {
    @Mock
    private EntrepreneurRepository entrepreneurRepository;

    @Mock
    private ImageComponent imageComponent;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EntrepreneurServiceImpl entrepreneurService;

    private final MultipartFile testMultipartFile = new MultipartFile() {
        @Override
        public String getName() {
            return "파일1";
        }

        @Override
        public String getOriginalFilename() {
            return "파일2";
        }

        @Override
        public String getContentType() {
            return "파일3";
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public long getSize() {
            return 5;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return new byte[0];
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return null;
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {

        }
    };

    @DisplayName("내 정보 수정 테스트 (이미지 업로드 X)")
    @Test
    void UpdateProfile() {
        // given
        Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
        EntrepreneurDto.UpdateRequest request = new EntrepreneurDto.UpdateRequest(
                "newNickname", "postal", "street", "detail", null, "1234abcd");

        when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId()))).thenReturn(java.util.Optional.of(entrepreneur));
        when(passwordEncoder.encode(eq(request.getPassword()))).thenReturn("encodedPassword");

        // when
        entrepreneurService.updateProfile(1L, null, request);

        // then
        assertEquals(request.getPhoneNumber(), entrepreneur.getPhoneNumber());
        assertEquals(request.getPostal(), entrepreneur.getAddress().getPostal());
        assertEquals(request.getStreetAddress(), entrepreneur.getAddress().getStreetAddress());
        assertEquals(request.getDetailAddress(), entrepreneur.getAddress().getDetailAddress());
        assertNotEquals(request.getImage(), entrepreneur.getImage());
        assertEquals(request.getPassword(), entrepreneur.getPassword());
    }

    @DisplayName("내 정보 수정 테스트 (이미지 업로드)")
    @Test
    void UpdateProfileWithImageUploadSuccess() {
        // given
        Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
        EntrepreneurDto.UpdateRequest request = new EntrepreneurDto.UpdateRequest(
                "newNickname", "postal", "street", "detail", null, "1234abcd");


        when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId()))).thenReturn(java.util.Optional.of(entrepreneur));
        when(passwordEncoder.encode(eq(request.getPassword()))).thenReturn("encodedPassword");
        when(imageComponent.uploadImageList(eq(List.of(testMultipartFile)), eq(ENTREPRENEUR_IMAGE_FOLDER)))
                .thenReturn(List.of("new_image"));
        // when
        entrepreneurService.updateProfile(1L, testMultipartFile, request);

        // then
        assertEquals(request.getPhoneNumber(), entrepreneur.getPhoneNumber());
        assertEquals(request.getPostal(), entrepreneur.getAddress().getPostal());
        assertEquals(request.getStreetAddress(), entrepreneur.getAddress().getStreetAddress());
        assertEquals(request.getDetailAddress(), entrepreneur.getAddress().getDetailAddress());
        assertEquals(request.getImage(), "new_image");
        assertEquals(entrepreneur.getImage(), "new_image");
        assertEquals(request.getPassword(), entrepreneur.getPassword());
    }

    @DisplayName("내 정보 수정 테스트 (이미지 업로드 실패)")
    @Test
    void UpdateProfileWithImageUploadFail() {
        // given
        Entrepreneur entrepreneur = TestEntrepreneurUtility.getEntrepreneur();
        EntrepreneurDto.UpdateRequest request = new EntrepreneurDto.UpdateRequest(
                "newNickname", "postal", "street", "detail", "123", "1234abcd");


        when(entrepreneurRepository.findByIdAndDeletedAtIsNull(eq(entrepreneur.getId()))).thenReturn(java.util.Optional.of(entrepreneur));
        when(passwordEncoder.encode(eq(request.getPassword()))).thenReturn("encodedPassword");
        when(imageComponent.uploadImageList(eq(List.of(testMultipartFile)), eq(ENTREPRENEUR_IMAGE_FOLDER)))
                .thenReturn(List.of());
        // when
        entrepreneurService.updateProfile(1L, testMultipartFile, request);

        // then
        assertEquals(request.getPhoneNumber(), entrepreneur.getPhoneNumber());
        assertEquals(request.getPostal(), entrepreneur.getAddress().getPostal());
        assertEquals(request.getStreetAddress(), entrepreneur.getAddress().getStreetAddress());
        assertEquals(request.getDetailAddress(), entrepreneur.getAddress().getDetailAddress());
        assertEquals(request.getImage(), null);
        assertNotEquals(entrepreneur.getImage(), null);
        assertEquals(request.getPassword(), entrepreneur.getPassword());
    }
}
