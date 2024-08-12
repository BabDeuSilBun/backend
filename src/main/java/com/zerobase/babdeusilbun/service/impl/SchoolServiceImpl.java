package com.zerobase.babdeusilbun.service.impl;

import com.zerobase.babdeusilbun.dto.SchoolDto.Information;
import com.zerobase.babdeusilbun.repository.SchoolRepository;
import com.zerobase.babdeusilbun.service.SchoolService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SchoolServiceImpl implements SchoolService {
  private final SchoolRepository schoolRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<Information> searchSchoolAndCampus(int page, int size, String schoolName) {
    return schoolRepository.searchSchoolNameByKeywords(schoolName.split(" +"), page, size);
  }
}
