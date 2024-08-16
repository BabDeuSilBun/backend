package com.zerobase.babdeusilbun.repository.custom;

import com.zerobase.babdeusilbun.dto.MajorDto;
import org.springframework.data.domain.Page;

public interface CustomMajorRepository {
    Page<MajorDto.Information> searchMajorNameByKeywords(String[] keywords, int page, int size);
}
