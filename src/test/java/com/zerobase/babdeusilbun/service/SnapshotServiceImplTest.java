package com.zerobase.babdeusilbun.service;

import static com.zerobase.babdeusilbun.enums.PurchaseType.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.zerobase.babdeusilbun.domain.IndividualPurchase;
import com.zerobase.babdeusilbun.domain.IndividualPurchasePayment;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Purchase;
import com.zerobase.babdeusilbun.domain.TeamPurchase;
import com.zerobase.babdeusilbun.domain.TeamPurchasePayment;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.SnapshotDto;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.repository.IndividualPurchasePaymentRepository;
import com.zerobase.babdeusilbun.repository.IndividualPurchaseRepository;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.PurchasePaymentRepository;
import com.zerobase.babdeusilbun.repository.PurchaseRepository;
import com.zerobase.babdeusilbun.repository.TeamPurchasePaymentRepository;
import com.zerobase.babdeusilbun.repository.TeamPurchaseRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.service.impl.SnapshotServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class SnapshotServiceImplTest {

  @InjectMocks
  private SnapshotServiceImpl snapshotService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private MeetingRepository meetingRepository;
  @Mock
  private PurchaseRepository purchaseRepository;
  @Mock
  private TeamPurchaseRepository teamPurchaseRepository;
  @Mock
  private IndividualPurchaseRepository individualPurchaseRepository;
  @Mock
  private IndividualPurchasePaymentRepository individualPurchasePaymentRepository;
  @Mock
  private TeamPurchasePaymentRepository teamPurchasePaymentRepository;
  @Mock
  private PurchasePaymentRepository purchasePaymentRepository;

  @Test
  @DisplayName("주문 후 공동 주문 스냅샷 리스트 조회 - 성공")
  void successGetTeamPurchaseSnapshots() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).purchaseType(DINING_TOGETHER).build();
    TeamPurchase purchase = TeamPurchase.builder().meeting(meeting).build();
    TeamPurchasePayment teamPurchasePayment =
        TeamPurchasePayment.builder().teamPurchase(purchase).build();

    Pageable pageable = PageRequest.of(0, 3);

    Page<TeamPurchasePayment> page = new PageImpl<>(List.of(teamPurchasePayment), pageable, 1);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(true);
    when(teamPurchasePaymentRepository.findByMeeting(meeting, pageable)).thenReturn(page);

    // when
    Page<SnapshotDto.SubPurchaseSnapshot> result =
        snapshotService.getTeamPurchaseSnapshots(1L, 1L, pageable);
    List<SnapshotDto.SubPurchaseSnapshot> content = result.getContent();

    // then
    assertThat(result.getSize()).isEqualTo(3);
    assertThat(result.getTotalElements()).isEqualTo(1);
  }

  @Test
  @DisplayName("주문 후 공동 주문 스냅샷 리스트 조회 - 실패 - 참가자 아님")
  void failGetTeamPurchaseSnapshots_not_participant() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).purchaseType(DINING_TOGETHER).build();
    TeamPurchase purchase = TeamPurchase.builder().meeting(meeting).build();
    TeamPurchasePayment teamPurchasePayment =
        TeamPurchasePayment.builder().teamPurchase(purchase).build();

    Pageable pageable = PageRequest.of(0, 3);

    Page<TeamPurchasePayment> page = new PageImpl<>(List.of(teamPurchasePayment), pageable, 1);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(false);
//    when(teamPurchasePaymentRepository.findByMeeting(meeting, pageable)).thenReturn(page);

    // when
    CustomException customException = assertThrows(CustomException.class,
        () -> snapshotService.getTeamPurchaseSnapshots(1L, 1L, pageable));

    // then
    assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.MEETING_PARTICIPANT_NOT_MATCH);
  }

  @Test
  @DisplayName("주문 후 공동 주문 스냅샷 리스트 조회 - 실패 - 같이 식사 아님")
  void failGetTeamPurchaseSnapshots_not_dining_together() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).purchaseType(DELIVERY_TOGETHER).build();
    TeamPurchase purchase = TeamPurchase.builder().meeting(meeting).build();
    TeamPurchasePayment teamPurchasePayment =
        TeamPurchasePayment.builder().teamPurchase(purchase).build();

    Pageable pageable = PageRequest.of(0, 3);

    Page<TeamPurchasePayment> page = new PageImpl<>(List.of(teamPurchasePayment), pageable, 1);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(true);
//    when(teamPurchasePaymentRepository.findByMeeting(meeting, pageable)).thenReturn(page);

    // when
    CustomException customException = assertThrows(CustomException.class,
        () -> snapshotService.getTeamPurchaseSnapshots(1L, 1L, pageable));

    // then
    assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.MEETING_TYPE_INVALID);
  }

  @Test
  @DisplayName("주문 후 개별 주문 스냅샷 리스트 조회 - 성공")
  void successGetIndividualPurchaseSnapshots() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).purchaseType(DELIVERY_TOGETHER).build();
    Purchase purchase = Purchase.builder().user(user).meeting(meeting).build();
    IndividualPurchase individualPurchase = IndividualPurchase.builder().purchase(purchase).build();
    IndividualPurchasePayment individualPurchasePayment =
        IndividualPurchasePayment.builder().individualPurchase(individualPurchase).build();

    Pageable pageable = PageRequest.of(0, 3);

    Page<IndividualPurchasePayment> page = new PageImpl<>(List.of(individualPurchasePayment), pageable, 1);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(true);
    when(individualPurchasePaymentRepository.findAllByUserAndMeeting(user, meeting, pageable)).thenReturn(page);

    // when
    Page<SnapshotDto.SubPurchaseSnapshot> result =
        snapshotService.getIndividualPurchaseSnapshots(1L, 1L, pageable);
    List<SnapshotDto.SubPurchaseSnapshot> content = result.getContent();

    // then
    assertThat(result.getSize()).isEqualTo(3);
    assertThat(result.getTotalElements()).isEqualTo(1);
  }

  @Test
  @DisplayName("주문 후 공동 주문 스냅샷 리스트 조회 - 실패 - 참가자 아님")
  void failGetIndividualPurchaseSnapshots_not_participant() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).purchaseType(DELIVERY_TOGETHER).build();
    Purchase purchase = Purchase.builder().user(user).meeting(meeting).build();
    IndividualPurchase individualPurchase = IndividualPurchase.builder().purchase(purchase).build();
    IndividualPurchasePayment individualPurchasePayment =
        IndividualPurchasePayment.builder().individualPurchase(individualPurchase).build();

    Pageable pageable = PageRequest.of(0, 3);

    Page<IndividualPurchasePayment> page = new PageImpl<>(List.of(individualPurchasePayment), pageable, 1);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(false);
//    when(individualPurchasePaymentRepository.findAllByUserAndMeeting(user, meeting, pageable)).thenReturn(page);

    // when
    CustomException customException = assertThrows(CustomException.class,
        () -> snapshotService.getIndividualPurchaseSnapshots(1L, 1L, pageable));

    // then
    assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.MEETING_PARTICIPANT_NOT_MATCH);
  }

  @Test
  @DisplayName("주문 후 공동 주문 스냅샷 리스트 조회 - 실패 - 같이 식사 아님")
  void failGetIndividualPurchaseSnapshots_not_dining_together() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).purchaseType(DINING_TOGETHER).build();
    Purchase purchase = Purchase.builder().user(user).meeting(meeting).build();
    IndividualPurchase individualPurchase = IndividualPurchase.builder().purchase(purchase).build();
    IndividualPurchasePayment individualPurchasePayment =
        IndividualPurchasePayment.builder().individualPurchase(individualPurchase).build();

    Pageable pageable = PageRequest.of(0, 3);

    Page<IndividualPurchasePayment> page = new PageImpl<>(List.of(individualPurchasePayment), pageable, 1);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(true);
//    when(individualPurchasePaymentRepository.findAllByUserAndMeeting(user, meeting, pageable)).thenReturn(page);

    // when
    CustomException customException = assertThrows(CustomException.class,
        () -> snapshotService.getIndividualPurchaseSnapshots(1L, 1L, pageable));

    // then
    assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.MEETING_TYPE_INVALID);
  }

}






