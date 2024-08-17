package com.zerobase.babdeusilbun.service.impl;

import com.zerobase.babdeusilbun.dto.MajorDto.Information;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.repository.MajorRepository;
import com.zerobase.babdeusilbun.repository.SchoolRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.MajorService;
import com.zerobase.babdeusilbun.service.SchoolService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MajorServiceImpl implements MajorService {
    private final MajorRepository majorRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Information> searchMajor(String majorName, int page, int size) {
        return majorRepository.searchMajorNameByKeywords(majorName.split(" +"), page, size);
    }
}
