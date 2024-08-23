package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.PointDto;
import com.zerobase.babdeusilbun.dto.PointDto.Response;
import com.zerobase.babdeusilbun.dto.PointDto.WithdrawalRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointService {

  Page<Response> getAllPointList(Long userId, Pageable pageable, String sortCriteria);

  void withdrawalPoint(Long userId, WithdrawalRequest request);
}
