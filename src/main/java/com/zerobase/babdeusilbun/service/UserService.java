package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  UserDto.MyPage getMyPage();

  UserDto.UpdateRequest updateProfile(Long userId, MultipartFile image, UserDto.UpdateRequest request);
}
