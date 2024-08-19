package com.zerobase.babdeusilbun.service;

import static com.zerobase.babdeusilbun.enums.MeetingStatus.*;
import static com.zerobase.babdeusilbun.enums.PurchaseType.*;
import static com.zerobase.babdeusilbun.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.zerobase.babdeusilbun.domain.IndividualPurchase;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Menu;
import com.zerobase.babdeusilbun.domain.Purchase;
import com.zerobase.babdeusilbun.domain.TeamPurchase;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.PurchaseDto.PurchaseResponse;
import com.zerobase.babdeusilbun.dto.PurchaseDto.PurchaseResponse.Item;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.repository.IndividualPurchaseRepository;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.PurchaseRepository;
import com.zerobase.babdeusilbun.repository.TeamPurchaseRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.service.impl.PurchaseServiceImpl;
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
class PurchaseServiceImplTest {

  @InjectMocks
  private PurchaseServiceImpl purchaseService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private MeetingRepository meetingRepository;
  @Mock
  private PurchaseRepository purchaseRepository;
  @Mock
  private IndividualPurchaseRepository individualPurchaseRepository;
  @Mock
  private TeamPurchaseRepository teamPurchaseRepository;

  @Test
  @DisplayName("주문 전 공동 주문 장바구니 조회 - 성공")
  void successGetTeamOrderCart() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).status(GATHERING).purchaseType(DINING_TOGETHER).build();
    Menu menu = Menu.builder().id(1L).price(1000L).build();
    TeamPurchase teamPurchase =
        TeamPurchase.builder().id(1L).meeting(meeting).quantity(2).menu(menu).build();
    Pageable pageable = PageRequest.of(0, 3);
    Page<TeamPurchase> page = new PageImpl<>(List.of(teamPurchase), pageable, 1);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(teamPurchaseRepository.findAllByMeeting(meeting, pageable)).thenReturn(page);
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(true);

    // when
    PurchaseResponse result = purchaseService.getTeamPurchaseCart(1L, 1L, pageable);
    Page<Item> itemPage = result.getItems();
    Item item = itemPage.getContent().getFirst();

    // then
    assertThat(result.getTotalFee()).isEqualTo(2000);
    assertThat(itemPage.getTotalPages()).isEqualTo(1);
    assertThat(itemPage.getSize()).isEqualTo(3);
    assertThat(itemPage.getTotalElements()).isEqualTo(1);
    assertThat(itemPage.getContent().size()).isEqualTo(1);
    assertThat(item.getPurchaseId()).isEqualTo(teamPurchase.getId());
    assertThat(item.getMenuId()).isEqualTo(menu.getId());
    assertThat(item.getName()).isEqualTo(menu.getName());
    assertThat(item.getPrice()).isEqualTo(menu.getPrice());
    assertThat(item.getDescription()).isEqualTo(menu.getDescription());
    assertThat(item.getQuantity()).isEqualTo(2);
  }

  @Test
  @DisplayName("주문 전 공동 주문 장바구니 조회 - 실패 - 참가자 아님")
  void failGetTeamOrderCart_not_participant() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).status(GATHERING).build();
    Menu menu = Menu.builder().id(1L).price(1000L).build();
    TeamPurchase teamPurchase =
        TeamPurchase.builder().id(1L).meeting(meeting).quantity(2).menu(menu).build();
    Pageable pageable = PageRequest.of(0, 3);
    Page<TeamPurchase> page = new PageImpl<>(List.of(teamPurchase), pageable, 1);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(false);
//    when(teamPurchaseRepository.findAllByMeeting(meeting, pageable)).thenReturn(page);

    // when
    CustomException customException = assertThrows(CustomException.class,
        () -> purchaseService.getTeamPurchaseCart(1L, 1L, pageable));

    // then
    assertThat(customException.getErrorCode()).isEqualTo(MEETING_PARTICIPANT_NOT_MATCH);
  }

  @Test
  @DisplayName("주문 전 공동 주문 장바구니 조회 - 실패 - 함께 식사 모임 아님")
  void failGetTeamOrderCart_not_dining_together() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).purchaseType(DELIVERY_TOGETHER).status(GATHERING).build();
    Menu menu = Menu.builder().id(1L).price(1000L).build();
    TeamPurchase teamPurchase =
        TeamPurchase.builder().id(1L).meeting(meeting).quantity(2).menu(menu).build();
    Pageable pageable = PageRequest.of(0, 3);
    Page<TeamPurchase> page = new PageImpl<>(List.of(teamPurchase), pageable, 1);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(true);
//    when(teamPurchaseRepository.findAllByMeeting(meeting, pageable)).thenReturn(page);

    // when
    CustomException customException = assertThrows(CustomException.class,
        () -> purchaseService.getTeamPurchaseCart(1L, 1L, pageable));

    // then
    assertThat(customException.getErrorCode()).isEqualTo(MEETING_TYPE_INVALID);
  }

  @Test
  @DisplayName("주문 전 공동 주문 장바구니 조회 - 실패 - 주문 전 모임 아님")
  void failGetTeamOrderCart_not_before_order() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).purchaseType(DINING_TOGETHER).status(MEETING_COMPLETED).build();
    Menu menu = Menu.builder().id(1L).price(1000L).build();
    TeamPurchase teamPurchase =
        TeamPurchase.builder().id(1L).meeting(meeting).quantity(2).menu(menu).build();
    Pageable pageable = PageRequest.of(0, 3);
    Page<TeamPurchase> page = new PageImpl<>(List.of(teamPurchase), pageable, 1);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(true);
//    when(teamPurchaseRepository.findAllByMeeting(meeting, pageable)).thenReturn(page);

    // when
    CustomException customException = assertThrows(CustomException.class,
        () -> purchaseService.getTeamPurchaseCart(1L, 1L, pageable));

    // then
    assertThat(customException.getErrorCode()).isEqualTo(MEETING_STATUS_INVALID);
  }


  @Test
  @DisplayName("주문 전 개별 주문 장바구니 조회 - 성공")
  void successGetIndividualOrderCart() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).status(GATHERING).purchaseType(DELIVERY_TOGETHER).build();
    Menu menu = Menu.builder().id(1L).price(1000L).build();
    Purchase purchase = Purchase.builder().id(1L).meeting(meeting).user(user).build();
    IndividualPurchase individualPurchase =
        IndividualPurchase.builder().id(1L).purchase(purchase).quantity(2).menu(menu).build();
    Pageable pageable = PageRequest.of(0, 3);
    Page<IndividualPurchase> page = new PageImpl<>(List.of(individualPurchase), pageable, 1);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.findByMeetingAndUser(meeting, user)).thenReturn(Optional.of(purchase));
    when(individualPurchaseRepository.findAllByPurchase(purchase, pageable)).thenReturn(page);
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(true);

    // when
    PurchaseResponse result = purchaseService.getIndividualPurchaseCart(1L, 1L, pageable);
    Page<Item> itemPage = result.getItems();
    Item item = itemPage.getContent().getFirst();

    // then
    assertThat(result.getTotalFee()).isEqualTo(2000);
    assertThat(itemPage.getTotalPages()).isEqualTo(1);
    assertThat(itemPage.getSize()).isEqualTo(3);
    assertThat(itemPage.getTotalElements()).isEqualTo(1);
    assertThat(itemPage.getContent().size()).isEqualTo(1);
    assertThat(item.getPurchaseId()).isEqualTo(individualPurchase.getId());
    assertThat(item.getMenuId()).isEqualTo(menu.getId());
    assertThat(item.getName()).isEqualTo(menu.getName());
    assertThat(item.getPrice()).isEqualTo(menu.getPrice());
    assertThat(item.getDescription()).isEqualTo(menu.getDescription());
    assertThat(item.getQuantity()).isEqualTo(2);
  }

  @Test
  @DisplayName("주문 전 개별 주문 장바구니 조회 - 실패 - 참가자 아님")
  void failGetIndividualOrderCart_not_participant() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).status(GATHERING).purchaseType(DELIVERY_TOGETHER).build();
    Menu menu = Menu.builder().id(1L).price(1000L).build();
    Purchase purchase = Purchase.builder().id(1L).meeting(meeting).user(user).build();
    IndividualPurchase individualPurchase =
        IndividualPurchase.builder().id(1L).purchase(purchase).quantity(2).menu(menu).build();
    Pageable pageable = PageRequest.of(0, 3);
    Page<IndividualPurchase> page = new PageImpl<>(List.of(individualPurchase), pageable, 1);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.findByMeetingAndUser(meeting, user)).thenReturn(Optional.of(purchase));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(false);
//    when(individualPurchaseRepository.findAllByPurchase(purchase, pageable)).thenReturn(page);

    // when
    CustomException customException = assertThrows(CustomException.class,
        () -> purchaseService.getIndividualPurchaseCart(1L, 1L, pageable));

    // then
    assertThat(customException.getErrorCode()).isEqualTo(MEETING_PARTICIPANT_NOT_MATCH);
  }

  @Test
  @DisplayName("주문 전 개별 주문 장바구니 조회 - 실패 - 함께 주문 아님")
  void failGetIndividualOrderCart_not_delivery_together() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).status(GATHERING).purchaseType(DINING_TOGETHER).build();
    Menu menu = Menu.builder().id(1L).price(1000L).build();
    Purchase purchase = Purchase.builder().id(1L).meeting(meeting).user(user).build();
    IndividualPurchase individualPurchase =
        IndividualPurchase.builder().id(1L).purchase(purchase).quantity(2).menu(menu).build();
    Pageable pageable = PageRequest.of(0, 3);
    Page<IndividualPurchase> page = new PageImpl<>(List.of(individualPurchase), pageable, 1);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.findByMeetingAndUser(meeting, user)).thenReturn(Optional.of(purchase));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(true);
//    when(individualPurchaseRepository.findAllByPurchase(purchase, pageable)).thenReturn(page);

    // when
    CustomException customException = assertThrows(CustomException.class,
        () -> purchaseService.getIndividualPurchaseCart(1L, 1L, pageable));

    // then
    assertThat(customException.getErrorCode()).isEqualTo(MEETING_TYPE_INVALID);
  }

  @Test
  @DisplayName("주문 전 개별 주문 장바구니 조회 - 실패 - 주문 전 모임 아님")
  void failGetIndividualOrderCart_not_before_order() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Meeting meeting = Meeting.builder().id(1L).status(PURCHASE_COMPLETED).purchaseType(DELIVERY_TOGETHER).build();
    Menu menu = Menu.builder().id(1L).price(1000L).build();
    Purchase purchase = Purchase.builder().id(1L).meeting(meeting).user(user).build();
    IndividualPurchase individualPurchase =
        IndividualPurchase.builder().id(1L).purchase(purchase).quantity(2).menu(menu).build();
    Pageable pageable = PageRequest.of(0, 3);
    Page<IndividualPurchase> page = new PageImpl<>(List.of(individualPurchase), pageable, 1);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.findByMeetingAndUser(meeting, user)).thenReturn(Optional.of(purchase));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(true);
//    when(individualPurchaseRepository.findAllByPurchase(purchase, pageable)).thenReturn(page);

    // when
    CustomException customException = assertThrows(CustomException.class,
        () -> purchaseService.getIndividualPurchaseCart(1L, 1L, pageable));

    // then
    assertThat(customException.getErrorCode()).isEqualTo(MEETING_STATUS_INVALID);
  }


}