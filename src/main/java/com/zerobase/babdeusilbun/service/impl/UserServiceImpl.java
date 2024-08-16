package com.zerobase.babdeusilbun.service.impl;

import static com.zerobase.babdeusilbun.exception.ErrorCode.USER_NOT_FOUND;
import static com.zerobase.babdeusilbun.util.ImageUtility.USER_IMAGE_FOLDER;

import com.zerobase.babdeusilbun.component.ImageComponent;
import com.zerobase.babdeusilbun.domain.Major;
import com.zerobase.babdeusilbun.domain.School;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.UserDto.UpdateRequest;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.repository.MajorRepository;
import com.zerobase.babdeusilbun.repository.SchoolRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.service.UserService;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final SchoolRepository schoolRepository;
  private final MajorRepository majorRepository;
  private final ImageComponent imageComponent;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public UpdateRequest updateProfile(Long userId, MultipartFile image, UpdateRequest request) {
    User user = userRepository.findByIdAndDeletedAtIsNotNull(userId)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    updateSchool(user, request);
    updateMajor(user, request);

    if (image != null) {
      updateImage(user, image, request);
    } else if (request.getImage() != null && request.getImage().isEmpty() && StringUtils.isNotBlank(user.getImage())) {
      imageComponent.deleteImageByUrl(user.getImage());
    }

    if (request.getPassword() != null) {
      request.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    user.update(request);
    return request;
  }

  private void updateSchool(User user, UpdateRequest request) {
    if (request.getSchoolId() == null) return;

    School school = schoolRepository.findById(request.getSchoolId()).orElse(null);
    if (school == null) {
      request.setSchoolId(null);
    }

    user.updateSchool(school);
  }

  private void updateMajor(User user, UpdateRequest request) {
    if (request.getMajorId() == null) return;

    Major major = majorRepository.findById(request.getMajorId()).orElse(null);
    if (major == null) {
      request.setMajorId(null);
      return;
    }

    user.updateMajor(major);
  }

  private void updateImage(User user, MultipartFile image, UpdateRequest request) {
    List<String> uploadUrlList = imageComponent.uploadImageList(List.of(image), USER_IMAGE_FOLDER);
    if (uploadUrlList.isEmpty()) {
      request.setImage(null);
      return;
    }

    if (StringUtils.isNotBlank(user.getImage())) {
      imageComponent.deleteImageByUrl(user.getImage());
    }

    request.setImage(uploadUrlList.getFirst());
  }
}