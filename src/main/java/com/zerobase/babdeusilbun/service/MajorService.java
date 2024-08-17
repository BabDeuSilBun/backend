package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.MajorDto;
import org.springframework.data.domain.Page;

public interface MajorService {
    Page<MajorDto.Information> searchMajor(String majorName, int page, int size);
}
