package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.EntrepreneurDto.MyPage;
import com.zerobase.babdeusilbun.dto.EntrepreneurDto.UpdateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface EntrepreneurService {
    MyPage getMyPage(Long entrepreneurId);

    UpdateRequest updateProfile(Long entrepreneurId, MultipartFile image, UpdateRequest updateRequest);

}
