package com.zerobase.babdeusilbun.service.impl;

import static com.zerobase.babdeusilbun.dto.PointDto.*;
import static com.zerobase.babdeusilbun.exception.ErrorCode.*;

import com.zerobase.babdeusilbun.domain.Point;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.PointDto;
import com.zerobase.babdeusilbun.dto.PointDto.Response;
import com.zerobase.babdeusilbun.enums.PointSortCriteria;
import com.zerobase.babdeusilbun.enums.PointType;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.repository.PointRepository;
import com.zerobase.babdeusilbun.repository.StoreRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.service.PointService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

  private final PointRepository pointRepository;
  private final UserRepository userRepository;
  private final StoreRepository storeRepository;

  @Override
  public Page<Response> getAllPointList(Long userId, Pageable pageable, String sortCriteria) {

    User findUser = findUserById(userId);

    List<PointType> sortType =
        List.of(PointSortCriteria.fromParameter(sortCriteria).getPointType());

    Page<Point> pointList = pointRepository.findSortedAllByUser(findUser, sortType, pageable);

    return pointList.map(this::mapToResponse);
  }

  private Response mapToResponse(Point point) {

    return Response.fromEntity(point, findStoreByPoint(point).getName());
  }

  private User findUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
  }

  private Store findStoreByPoint(Point point) {
    return storeRepository.findStoreByPoint(point)
        .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));
  }


}
