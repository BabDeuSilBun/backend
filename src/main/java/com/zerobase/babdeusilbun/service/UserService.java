package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.domain.Address;
import com.zerobase.babdeusilbun.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  UserDto.MyPage getMyPage(Long userId);

  UserDto.Profile getUserProfile(Long userId);

  UserDto.UpdateRequest updateProfile(Long userId, MultipartFile image, UserDto.UpdateRequest request);

  UserDto.UpdateAddress updateAddress(Long userId, UserDto.UpdateAddress updateAddress);
}
