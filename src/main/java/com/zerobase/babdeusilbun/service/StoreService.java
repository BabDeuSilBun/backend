package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.StoreDto.CreateRequest;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface StoreService {
  int createStore(Long entrepreneurId, List<MultipartFile> images, CreateRequest request);
}
