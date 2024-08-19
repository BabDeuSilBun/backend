package com.zerobase.babdeusilbun.service.impl;

import static com.zerobase.babdeusilbun.exception.ErrorCode.*;

import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.PurchaseDto.TeamPurchaseResponse;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.PurchaseRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

  private final PurchaseRepository purchaseRepository;
  private final UserRepository userRepository;
  private final MeetingRepository meetingRepository;

  @Override
  public TeamPurchaseResponse getTeamOrderCart(Long userId, Long meetingId, Pageable pageable) {



    return null;
  }

  private User findUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
  }
}
