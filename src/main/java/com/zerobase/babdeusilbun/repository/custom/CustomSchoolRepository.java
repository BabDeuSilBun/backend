package com.zerobase.babdeusilbun.repository.custom;

import com.zerobase.babdeusilbun.dto.SchoolDto.Information;
import org.springframework.data.domain.Page;

public interface CustomSchoolRepository {
  Page<Information> searchSchoolNameByKeywords(String[] keywords, int page, int size);
  Page<Information> searchCampusBySchool(Information standard, int page, int size);
}
