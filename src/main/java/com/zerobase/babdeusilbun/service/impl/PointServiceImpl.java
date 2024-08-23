package com.zerobase.babdeusilbun.service.impl;

import static com.zerobase.babdeusilbun.annotation.RedissonLockKeyType.*;
import static com.zerobase.babdeusilbun.dto.PointDto.*;
import static com.zerobase.babdeusilbun.exception.ErrorCode.*;

import com.zerobase.babdeusilbun.annotation.RedissonLock;
import com.zerobase.babdeusilbun.annotation.RedissonLockKeyType;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

  @Override
  @RedissonLock(key = PAYMENT, value = "userId")
  public void withdrawalPoint(Long userId, WithdrawalRequest request) {
    User findUser = findUserById(userId);

    verifyPointEnough(request, findUser);

    Point point = Point.builder()
        .user(findUser)
        .type(PointType.MINUS)
        .amount(request.getAmount().longValue())
        .content("포인트 인출")
        .build();
    pointRepository.save(point);
    findUser.minusPoint(request.getAmount().longValue());
  }

  private void verifyPointEnough(WithdrawalRequest request, User findUser) {
    if (findUser.getPoint() < request.getAmount()) {
      throw new CustomException(POINT_SHORTAGE);
    }
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
