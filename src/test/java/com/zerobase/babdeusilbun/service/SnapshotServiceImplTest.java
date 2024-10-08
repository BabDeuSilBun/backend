package com.zerobase.babdeusilbun.service;

import static com.zerobase.babdeusilbun.enums.PurchaseType.*;
import static com.zerobase.babdeusilbun.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerobase.babdeusilbun.domain.IndividualPurchase;
import com.zerobase.babdeusilbun.domain.IndividualPurchasePayment;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Payment;
import com.zerobase.babdeusilbun.domain.Purchase;
import com.zerobase.babdeusilbun.domain.PurchasePayment;
import com.zerobase.babdeusilbun.domain.TeamPurchase;
import com.zerobase.babdeusilbun.domain.TeamPurchasePayment;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.SnapshotDto;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.repository.IndividualPurchasePaymentRepository;
import com.zerobase.babdeusilbun.repository.IndividualPurchaseRepository;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.PaymentRepository;
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
  private PaymentRepository paymentRepository;
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
    Page<TeamPurchasePayment> result =
        snapshotService.getTeamPurchaseSnapshots(1L, 1L, pageable);
    List<TeamPurchasePayment> content = result.getContent();

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
    assertThat(customException.getErrorCode()).isEqualTo(MEETING_PARTICIPANT_NOT_MATCH);
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
    assertThat(customException.getErrorCode()).isEqualTo(MEETING_TYPE_INVALID);
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

    Page<IndividualPurchasePayment> page = new PageImpl<>(List.of(individualPurchasePayment),
        pageable, 1);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(true);
    when(individualPurchasePaymentRepository.findAllByUserAndMeeting(user, meeting,
        pageable)).thenReturn(page);

    // when
    Page<IndividualPurchasePayment> result =
        snapshotService.getIndividualPurchaseSnapshots(1L, 1L, pageable);
    List<IndividualPurchasePayment> content = result.getContent();

    // then
    assertThat(result.getSize()).isEqualTo(3);
    assertThat(result.getTotalElements()).isEqualTo(1);
  }

  @Test
  @DisplayName("주문 후 개별 주문 스냅샷 리스트 조회 - 실패 - 참가자 아님")
  void failGetIndividualPurchaseSnapshots_not_participant() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).purchaseType(DELIVERY_TOGETHER).build();
    Purchase purchase = Purchase.builder().user(user).meeting(meeting).build();
    IndividualPurchase individualPurchase = IndividualPurchase.builder().purchase(purchase).build();
    IndividualPurchasePayment individualPurchasePayment =
        IndividualPurchasePayment.builder().individualPurchase(individualPurchase).build();

    Pageable pageable = PageRequest.of(0, 3);

    Page<IndividualPurchasePayment> page = new PageImpl<>(List.of(individualPurchasePayment),
        pageable, 1);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(false);
//    when(individualPurchasePaymentRepository.findAllByUserAndMeeting(user, meeting, pageable)).thenReturn(page);

    // when
    CustomException customException = assertThrows(CustomException.class,
        () -> snapshotService.getIndividualPurchaseSnapshots(1L, 1L, pageable));

    // then
    assertThat(customException.getErrorCode()).isEqualTo(MEETING_PARTICIPANT_NOT_MATCH);
  }

  @Test
  @DisplayName("주문 후 개별 주문 스냅샷 리스트 조회 - 실패 - 같이 식사 아님")
  void failGetIndividualPurchaseSnapshots_not_dining_together() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).purchaseType(DINING_TOGETHER).build();
    Purchase purchase = Purchase.builder().user(user).meeting(meeting).build();
    IndividualPurchase individualPurchase = IndividualPurchase.builder().purchase(purchase).build();
    IndividualPurchasePayment individualPurchasePayment =
        IndividualPurchasePayment.builder().individualPurchase(individualPurchase).build();

    Pageable pageable = PageRequest.of(0, 3);

    Page<IndividualPurchasePayment> page = new PageImpl<>(List.of(individualPurchasePayment),
        pageable, 1);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(true);
//    when(individualPurchasePaymentRepository.findAllByUserAndMeeting(user, meeting, pageable)).thenReturn(page);

    // when
    CustomException customException = assertThrows(CustomException.class,
        () -> snapshotService.getIndividualPurchaseSnapshots(1L, 1L, pageable));

    // then
    assertThat(customException.getErrorCode()).isEqualTo(MEETING_TYPE_INVALID);
  }

  @Test
  @DisplayName("주문 후 주문 스냅샷 조회 - 성공")
  void successGetPurchaseSnapshot() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).build();

    PurchasePayment purchasePayment = PurchasePayment.builder().id(1L).build();

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(true);
    when(purchasePaymentRepository.findByMeetingAndUser(meeting, user))
        .thenReturn(Optional.of(purchasePayment));

    // when
    PurchasePayment purchaseSnapshot = snapshotService.getPurchaseSnapshot(1L, 1L);

    // then
    verify(purchasePaymentRepository, times(1)).findByMeetingAndUser(meeting, user);
    assertThat(purchaseSnapshot.getId()).isEqualTo(purchasePayment.getId());
  }

  @Test
  @DisplayName("주문 후 주문 스냅샷 조회 - 실패 - 참가자 아님")
  void failGetPurchaseSnapshot_not_participant() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).build();

    PurchasePayment purchasePayment = PurchasePayment.builder().id(1L).build();

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(false);
//    when(purchasePaymentRepository.findByMeetingAndUser(meeting, user))
//        .thenReturn(Optional.of(purchasePayment));

    // when
    CustomException customException = assertThrows(CustomException.class,
        () -> snapshotService.getPurchaseSnapshot(1L, 1L));

    // then
    assertThat(customException.getErrorCode()).isEqualTo(MEETING_PARTICIPANT_NOT_MATCH);
  }

  @Test
  @DisplayName("결제 스냅샷 조회")
  void successGetPaymentSnapshot() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).purchaseType(DINING_TOGETHER).build();
    Payment payment = Payment.builder().id(1L).build();

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(true);
    when(paymentRepository.findByMeetingAndUser(meeting, user)).thenReturn(Optional.of(payment));

    // when
    Payment paymentSnapshot = snapshotService.getPaymentSnapshot(1L, 1L);

    // then
    assertThat(paymentSnapshot.getId()).isEqualTo(payment.getId());
  }

  @Test
  @DisplayName("결제 스냅샷 조회 - 실패 - 참가자 아님")
  void failGetPaymentSnapshot_not_participant() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).purchaseType(DINING_TOGETHER).build();
    Payment payment = Payment.builder().id(1L).build();

    PurchasePayment purchasePayment = PurchasePayment.builder().id(1L).build();

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(false);
//    when(paymentRepository.findByMeetingAndUser(meeting, user)).thenReturn(Optional.of(payment));

    // when
    CustomException customException = assertThrows(CustomException.class,
        () -> snapshotService.getPaymentSnapshot(1L, 1L));

    // then
    assertThat(customException.getErrorCode()).isEqualTo(MEETING_PARTICIPANT_NOT_MATCH);
  }

}






