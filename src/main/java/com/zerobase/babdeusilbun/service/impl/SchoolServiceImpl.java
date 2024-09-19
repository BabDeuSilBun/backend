package com.zerobase.babdeusilbun.service.impl;

import com.zerobase.babdeusilbun.dto.SchoolDto.Information;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.repository.SchoolRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.SchoolService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SchoolServiceImpl implements SchoolService {
  private final SchoolRepository schoolRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<Information> searchSchoolAndCampus(String schoolName, int page, int size) {
    return schoolRepository.searchSchoolNameByKeywords(schoolName.split(" +"), page, size);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Information> searchCampusBySchool(CustomUserDetails user, Long schoolId, int page, int size) {
    if (schoolId == null || schoolId == 0L) {
      schoolId = userRepository.findById(user.getId())
          .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))
          .getSchool().getId();
    }

    Information standard = Information.fromEntity(
        schoolRepository.findById(schoolId).orElseThrow(() -> new CustomException(ErrorCode.SCHOOL_NOT_FOUND))
    );

    return schoolRepository.searchCampusBySchool(standard, page, size);
  }
}
