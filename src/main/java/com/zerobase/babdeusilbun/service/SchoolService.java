package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.SchoolDto.Information;
import org.springframework.data.domain.Page;

public interface SchoolService {
  Page<Information> searchSchoolAndCampus(int page, int size, String schoolName);
}
