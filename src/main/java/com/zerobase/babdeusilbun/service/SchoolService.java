package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.SchoolDto.Information;
import org.springframework.data.domain.Page;

public interface SchoolService {
  Page<Information> searchSchoolAndCampus(String schoolName, int page, int size);

  Page<Information> searchCampusBySchool(Long schoolId, int page, int size);
}
