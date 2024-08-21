package com.zerobase.babdeusilbun.service.impl;

import static com.zerobase.babdeusilbun.dto.SnapshotDto.*;
import static com.zerobase.babdeusilbun.enums.PurchaseType.*;
import static com.zerobase.babdeusilbun.exception.ErrorCode.MEETING_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.MEETING_PARTICIPANT_NOT_MATCH;
import static com.zerobase.babdeusilbun.exception.ErrorCode.MEETING_TYPE_INVALID;
import static com.zerobase.babdeusilbun.exception.ErrorCode.PURCHASE_PAYMENT_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.USER_NOT_FOUND;

import com.zerobase.babdeusilbun.domain.IndividualPurchasePayment;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Point;
import com.zerobase.babdeusilbun.domain.PurchasePayment;
import com.zerobase.babdeusilbun.domain.TeamPurchasePayment;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.SnapshotDto;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.repository.IndividualPurchasePaymentRepository;
import com.zerobase.babdeusilbun.repository.IndividualPurchaseRepository;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.PointRepository;
import com.zerobase.babdeusilbun.repository.PurchasePaymentRepository;
import com.zerobase.babdeusilbun.repository.PurchaseRepository;
import com.zerobase.babdeusilbun.repository.TeamPurchasePaymentRepository;
import com.zerobase.babdeusilbun.repository.TeamPurchaseRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.service.SnapshotService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SnapshotServiceImpl implements SnapshotService {

  private final UserRepository userRepository;
  private final MeetingRepository meetingRepository;
  private final PurchaseRepository purchaseRepository;
  private final TeamPurchaseRepository teamPurchaseRepository;
  private final IndividualPurchaseRepository individualPurchaseRepository;
  private final PointRepository pointRepository;
  private final TeamPurchasePaymentRepository teamPurchasePaymentRepository;
  private final IndividualPurchasePaymentRepository individualPurchasePaymentRepository;
  private final PurchasePaymentRepository purchasePaymentRepository;

  @Override
  public Page<TeamPurchasePayment> getTeamPurchaseSnapshots
      (Long userId, Long meetingId, Pageable pageable) {

    User findUser = findUserById(userId);
    Meeting findMeeting = findMeetingById(meetingId);

    // 해당 유저가 모임의 참가자 인지 확인
    verifyMeetingParticipant(findMeeting, findUser);

    // 해당 모임이 같이 식사 타입인지 확인
    verifyDiningTogether(findMeeting);

    return teamPurchasePaymentRepository.findByMeeting(findMeeting, pageable);
  }

  @Override
  public Page<IndividualPurchasePayment> getIndividualPurchaseSnapshots
      (Long userId, Long meetingId, Pageable pageable) {

    User findUser = findUserById(userId);
    Meeting findMeeting = findMeetingById(meetingId);

    // 해당 유저가 모임의 참가자 인지 확인
    verifyMeetingParticipant(findMeeting, findUser);

    // 해당 모임이 같이 배달 타입인지 확인
    verifyDeliveryTogether(findMeeting);

    return individualPurchasePaymentRepository
        .findAllByUserAndMeeting(findUser, findMeeting, pageable);
  }

  @Override
  public PurchasePayment getPurchaseSnapshot(Long userId, Long meetingId) {

    User findUser = findUserById(userId);
    Meeting findMeeting = findMeetingById(meetingId);

    // 해당 유저가 모임의 참가자 인지 확인
    verifyMeetingParticipant(findMeeting, findUser);

    return purchasePaymentRepository.findByMeetingAndUser(findMeeting, findUser)
        .orElseThrow(() -> new CustomException(PURCHASE_PAYMENT_NOT_FOUND));
  }

  @Override
  public Page<Point> getPointSnapshotList(Long userId, Pageable pageable) {

    return pointRepository.findAllByUser(findUserById(userId), pageable);
  }

  private void verifyDeliveryTogether(Meeting findMeeting) {
    if (findMeeting.getPurchaseType() != DELIVERY_TOGETHER) {
      throw new CustomException(MEETING_TYPE_INVALID);
    }
  }

  private void verifyDiningTogether(Meeting findMeeting) {
    if (findMeeting.getPurchaseType() != DINING_TOGETHER) {
      throw new CustomException(MEETING_TYPE_INVALID);
    }
  }

  private void verifyMeetingParticipant(Meeting meeting, User participant) {
    if (!purchaseRepository.existsByMeetingAndUser(meeting, participant)) {
      throw new CustomException(MEETING_PARTICIPANT_NOT_MATCH);
    }
  }

  private Meeting findMeetingById(Long meetingId) {
    return meetingRepository.findById(meetingId)
        .orElseThrow(() -> new CustomException(MEETING_NOT_FOUND));
  }

  private User findUserById(Long userId) {
    return userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
  }

}
