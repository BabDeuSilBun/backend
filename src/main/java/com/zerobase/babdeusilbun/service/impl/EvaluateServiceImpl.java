package com.zerobase.babdeusilbun.service.impl;

import static com.zerobase.babdeusilbun.enums.MeetingStatus.*;
import static com.zerobase.babdeusilbun.exception.ErrorCode.*;

import com.zerobase.babdeusilbun.domain.Evaluate;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.EvaluateDto;
import com.zerobase.babdeusilbun.dto.EvaluateDto.EvaluateParticipantRequest;
import com.zerobase.babdeusilbun.enums.EvaluateBadge;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.repository.EvaluateRepository;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.PurchaseRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.service.EvaluateService;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EvaluateServiceImpl implements EvaluateService {

  private final UserRepository userRepository;
  private final MeetingRepository meetingRepository;
  private final EvaluateRepository evaluateRepository;
  private final PurchaseRepository purchaseRepository;

  @Override
  @Transactional(readOnly = true)
  public EvaluateDto.MyEvaluates getEvaluates(Long userId) {
    List<EvaluateDto.PositiveEvaluate> positiveEvaluateList = evaluateRepository.findPositiveEvaluatesByUserId(
        userId);
    List<EvaluateDto.NegativeEvaluate> negativeEvaluateList = evaluateRepository.findNegativeEvaluatesByUserId(
        userId);

    EvaluateDto.MyEvaluates evaluates = EvaluateDto.MyEvaluates.builder()
        .positiveEvaluate(positiveEvaluateList).negativeEvaluate(negativeEvaluateList).build();
    return evaluates;
  }

  @Override
  public List<Evaluate> evaluateParticipant
      (EvaluateParticipantRequest request, Long userId, Long meetingId, Long participantId) {

    User findEvaluator = findUserById(userId); // 평가자
    User findEvaluatee = findUserById(participantId); // 평가 대상
    Meeting findMeeting = findMeetingById(meetingId);

    // 모임이 완료된 상태인지 확인
    verifyMeetingIsComplete(findMeeting);

    // 평가자와 평가 대상이 모임에 참여한 사용자인지 확인
    verifyMeetingParticipant(findEvaluator);
    verifyMeetingParticipant(findEvaluatee);

    // 이미 평가했는지 확인
    verifyAlreadyEvaluate(findMeeting, userId, participantId);

    // 엔티티 생성
    return evaluateRepository.saveAll(
        mapToEvaluate(request, findMeeting, userId, participantId)
    );
  }

  private List<Evaluate> mapToEvaluate
      (EvaluateParticipantRequest request, Meeting meeting, Long evaluatorId, Long evaluateeId) {

    List<Evaluate> evaluateList = new ArrayList<>();
    evaluateList.addAll
        (createNewEvaluate(request.getPositiveEvaluate(), meeting, evaluatorId, evaluateeId));
    evaluateList.addAll
        (createNewEvaluate(request.getNegativeEvaluate(), meeting, evaluatorId, evaluateeId));

    return evaluateList;
  }

  private List<Evaluate> createNewEvaluate
      (List<EvaluateBadge> badges, Meeting meeting, Long evaluatorId, Long evaluateeId) {

    return badges.stream()
        .map(badge -> Evaluate.builder()
            .meeting(meeting)
            .evaluatorId(evaluatorId).evaluateeId(evaluateeId)
            .content(badge)
            .build())
        .toList();
  }

  private void verifyAlreadyEvaluate(Meeting findMeeting, Long evaluatorId, Long evaluateeId) {
    if (evaluateRepository.existsEvaluate(findMeeting, evaluatorId, evaluateeId)) {
      throw new CustomException(EVALUATE_ALREADY_EXIST);
    }
  }

  private void verifyMeetingParticipant(User findEvaluator) {
    if (!purchaseRepository.existsByUser(findEvaluator)) {
      throw new CustomException(MEETING_PARTICIPANT_NOT_MATCH);
    }
  }

  private void verifyMeetingIsComplete(Meeting findMeeting) {
    if (findMeeting.getStatus() != MEETING_COMPLETED) {
      throw new CustomException(MEETING_STATUS_INVALID);
    }
  }

  private User findUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
  }

  private Meeting findMeetingById(Long meetingId) {
    return meetingRepository.findById(meetingId)
        .orElseThrow(() -> new CustomException(MEETING_NOT_FOUND));
  }
}
