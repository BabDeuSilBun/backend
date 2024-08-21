package com.zerobase.babdeusilbun.service;


import static com.zerobase.babdeusilbun.enums.EvaluateBadge.*;
import static com.zerobase.babdeusilbun.enums.MeetingStatus.*;
import static com.zerobase.babdeusilbun.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerobase.babdeusilbun.domain.Evaluate;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Purchase;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.EvaluateDto.EvaluateParticipantRequest;
import com.zerobase.babdeusilbun.enums.EvaluateBadge;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.repository.EvaluateRepository;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.PurchaseRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.service.impl.EvaluateServiceImpl;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EvaluateServiceTest {

  @InjectMocks
  private EvaluateServiceImpl evaluateService;

  @Mock
  private UserRepository userRepository;
  @Mock
  private MeetingRepository meetingRepository;
  @Mock
  private EvaluateRepository evaluateRepository;
  @Mock
  private PurchaseRepository purchaseRepository;

  @Test
  @DisplayName("모임 후 회원 평가")
  void successEvaluate() throws Exception {
    // given
    User evaluator = User.builder().id(1L).email("evaluator").build();
    User evaluatee = User.builder().id(2L).email("evaluatee").build();
    Meeting meeting = Meeting.builder().id(1L).status(MEETING_COMPLETED).build();
//    Purchase purchase1 = Purchase.builder().id(1L).meeting(meeting).user(evaluator).build();
//    Purchase purchase2 = Purchase.builder().id(2L).meeting(meeting).user(evaluatee).build();

    List<EvaluateBadge> positive = List.of(GOOD_RESPONSE, GOOD_COMMUNICATION);
    List<EvaluateBadge> negative = List.of(BAD_RESPONSE, BAD_TOGETHER);
    EvaluateParticipantRequest request = EvaluateParticipantRequest.builder()
        .positiveEvaluate(positive).negativeEvaluate(negative).build();

    when(userRepository.findById(1L)).thenReturn(Optional.of(evaluator));
    when(userRepository.findById(2L)).thenReturn(Optional.of(evaluatee));
    when(meetingRepository.findById(anyLong())).thenReturn(Optional.of(meeting));
    when(evaluateRepository.existsEvaluate(meeting, 1L, 2L)).thenReturn(false);
    when(purchaseRepository.existsByMeetingAndUser(meeting, evaluator)).thenReturn(true);
    when(purchaseRepository.existsByMeetingAndUser(meeting, evaluatee)).thenReturn(true);

    // when
    List<Evaluate> result = evaluateService.evaluateParticipant(request, 1L, 1L, 2L);

    // then
    verify(userRepository, times(1)).findById(1L);
    verify(userRepository, times(1)).findById(2L);
    verify(meetingRepository, times(1)).findById(1L);
    verify(evaluateRepository, times(1)).saveAll(anyList());
  }

  @Test
  @DisplayName("모임 후 회원 평가 - 실패 - 모임 상태 완료 아님")
  void failEvaluate_meeting_status() throws Exception {
    // given
    User evaluator = User.builder().id(1L).email("evaluator").build();
    User evaluatee = User.builder().id(2L).email("evaluatee").build();
    Meeting meeting = Meeting.builder().id(1L).status(GATHERING).build();

//    List<EvaluateBadge> positive = List.of(GOOD_RESPONSE, GOOD_COMMUNICATION);
//    List<EvaluateBadge> negative = List.of(BAD_RESPONSE, BAD_TOGETHER);
    EvaluateParticipantRequest request = EvaluateParticipantRequest.builder()
//        .positiveEvaluate(positive).negativeEvaluate(negative)
        .build();

    when(userRepository.findById(1L)).thenReturn(Optional.of(evaluator));
    when(userRepository.findById(2L)).thenReturn(Optional.of(evaluatee));
    when(meetingRepository.findById(anyLong())).thenReturn(Optional.of(meeting));
//    when(evaluateRepository.existsEvaluate(meeting, 1L, 2L)).thenReturn(false);
//    when(purchaseRepository.existsByUser(evaluator)).thenReturn(true);
//    when(purchaseRepository.existsByUser(evaluatee)).thenReturn(true);

    // when
    CustomException customException = assertThrows(
        CustomException.class,
        () -> evaluateService.evaluateParticipant(request, 1L, 1L, 2L)
    );

    // then
    assertThat(customException.getErrorCode()).isEqualTo(MEETING_STATUS_INVALID);
  }

  @Test
  @DisplayName("모임 후 회원 평가 - 실패 - 평가자가 참가자 아님")
  void failEvaluate_evaluator() throws Exception {
    // given
    User evaluator = User.builder().id(1L).email("evaluator").build();
    User evaluatee = User.builder().id(2L).email("evaluatee").build();
    Meeting meeting = Meeting.builder().id(1L).status(MEETING_COMPLETED).build();

//    List<EvaluateBadge> positive = List.of(GOOD_RESPONSE, GOOD_COMMUNICATION);
//    List<EvaluateBadge> negative = List.of(BAD_RESPONSE, BAD_TOGETHER);
    EvaluateParticipantRequest request = EvaluateParticipantRequest.builder()
//        .positiveEvaluate(positive).negativeEvaluate(negative)
        .build();

    when(userRepository.findById(1L)).thenReturn(Optional.of(evaluator));
    when(userRepository.findById(2L)).thenReturn(Optional.of(evaluatee));
    when(meetingRepository.findById(anyLong())).thenReturn(Optional.of(meeting));
    when(purchaseRepository.existsByMeetingAndUser(meeting, evaluator)).thenReturn(false);
//    when(purchaseRepository.existsByUser(evaluatee)).thenReturn(true);
//    when(evaluateRepository.existsEvaluate(meeting, 1L, 2L)).thenReturn(false);

    // when
    CustomException customException = assertThrows(
        CustomException.class,
        () -> evaluateService.evaluateParticipant(request, 1L, 1L, 2L)
    );

    // then
    assertThat(customException.getErrorCode()).isEqualTo(MEETING_PARTICIPANT_NOT_MATCH);
  }

  @Test
  @DisplayName("모임 후 회원 평가 - 실패 - 평가 대상이 참가자 아님")
  void failEvaluate_evaluatee() throws Exception {
    // given
    User evaluator = User.builder().id(1L).email("evaluator").build();
    User evaluatee = User.builder().id(2L).email("evaluatee").build();
    Meeting meeting = Meeting.builder().id(1L).status(MEETING_COMPLETED).build();

//    List<EvaluateBadge> positive = List.of(GOOD_RESPONSE, GOOD_COMMUNICATION);
//    List<EvaluateBadge> negative = List.of(BAD_RESPONSE, BAD_TOGETHER);
    EvaluateParticipantRequest request = EvaluateParticipantRequest.builder()
//        .positiveEvaluate(positive).negativeEvaluate(negative)
        .build();

    when(userRepository.findById(1L)).thenReturn(Optional.of(evaluator));
    when(userRepository.findById(2L)).thenReturn(Optional.of(evaluatee));
    when(meetingRepository.findById(anyLong())).thenReturn(Optional.of(meeting));
    when(purchaseRepository.existsByMeetingAndUser(meeting, evaluator)).thenReturn(true);
    when(purchaseRepository.existsByMeetingAndUser(meeting, evaluatee)).thenReturn(false);
//    when(evaluateRepository.existsEvaluate(meeting, 1L, 2L)).thenReturn(false);

    // when
    CustomException customException = assertThrows(
        CustomException.class,
        () -> evaluateService.evaluateParticipant(request, 1L, 1L, 2L)
    );

    // then
    assertThat(customException.getErrorCode()).isEqualTo(MEETING_PARTICIPANT_NOT_MATCH);
  }

  @Test
  @DisplayName("모임 후 회원 평가 - 실패 - 이미 평가 했음")
  void failEvaluate_already_done() throws Exception {
    // given
    User evaluator = User.builder().id(1L).email("evaluator").build();
    User evaluatee = User.builder().id(2L).email("evaluatee").build();
    Meeting meeting = Meeting.builder().id(1L).status(MEETING_COMPLETED).build();

//    List<EvaluateBadge> positive = List.of(GOOD_RESPONSE, GOOD_COMMUNICATION);
//    List<EvaluateBadge> negative = List.of(BAD_RESPONSE, BAD_TOGETHER);
    EvaluateParticipantRequest request = EvaluateParticipantRequest.builder()
//        .positiveEvaluate(positive).negativeEvaluate(negative)
        .build();

    when(userRepository.findById(1L)).thenReturn(Optional.of(evaluator));
    when(userRepository.findById(2L)).thenReturn(Optional.of(evaluatee));
    when(meetingRepository.findById(anyLong())).thenReturn(Optional.of(meeting));
    when(purchaseRepository.existsByMeetingAndUser(meeting, evaluator)).thenReturn(true);
    when(purchaseRepository.existsByMeetingAndUser(meeting, evaluatee)).thenReturn(true);
    when(evaluateRepository.existsEvaluate(meeting, 1L, 2L)).thenReturn(true);

    // when
    CustomException customException = assertThrows(
        CustomException.class,
        () -> evaluateService.evaluateParticipant(request, 1L, 1L, 2L)
    );

    // then
    assertThat(customException.getErrorCode()).isEqualTo(EVALUATE_ALREADY_EXIST);
  }

}