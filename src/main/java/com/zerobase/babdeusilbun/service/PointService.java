package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.PointDto.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointService {

  Page<Response> getAllPointList(Long userId, Pageable pageable, String sortCriteria);
}
