package com.zerobase.babdeusilbun.service.impl;

import static com.zerobase.babdeusilbun.enums.MeetingStatus.*;
import static com.zerobase.babdeusilbun.enums.PurchaseStatus.*;
import static com.zerobase.babdeusilbun.enums.PurchaseType.*;
import static com.zerobase.babdeusilbun.exception.ErrorCode.*;

import com.zerobase.babdeusilbun.domain.IndividualPurchase;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Menu;
import com.zerobase.babdeusilbun.domain.Purchase;
import com.zerobase.babdeusilbun.domain.TeamPurchase;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.PurchaseDto;
import com.zerobase.babdeusilbun.dto.PurchaseDto.DeliveryFeeResponse;
import com.zerobase.babdeusilbun.dto.PurchaseDto.PurchaseResponse;
import com.zerobase.babdeusilbun.dto.PurchaseDto.PurchaseResponse.Item;
import com.zerobase.babdeusilbun.enums.PurchaseStatus;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.repository.IndividualPurchaseRepository;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.PurchaseRepository;
import com.zerobase.babdeusilbun.repository.TeamPurchaseRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.service.PurchaseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

  private final UserRepository userRepository;
  private final MeetingRepository meetingRepository;
  private final PurchaseRepository purchaseRepository;
  private final TeamPurchaseRepository teamPurchaseRepository;
  private final IndividualPurchaseRepository individualPurchaseRepository;

  /**
   * 주문 전 공동 주문 장바구니 조회
   */
  @Override
  public PurchaseResponse getTeamPurchaseCart(Long meetingId, Pageable pageable) {

    Meeting findMeeting = findMeetingById(meetingId);

    // 해당 모임이 함께 식사 모임인지 확인
    verifyDiningTogether(findMeeting);

    // 주문 전 모임인지 확인
    verifyBeforeOrder(findMeeting);

    return mapToTeamResponse(teamPurchaseRepository.findAllByMeeting(findMeeting, pageable));
  }

  /**
   * 주문 전 개별 주문 장바구니 조회
   */
  @Override
  public PurchaseResponse getIndividualPurchaseCart(Long userId, Long meetingId,
      Pageable pageable) {
    User findUser = findUserById(userId);
    Meeting findMeeting = findMeetingById(meetingId);
    Purchase findPurchase = findPurchaseByUserAndMeeting(findUser, findMeeting);

    // 해당 모임의 참가자 인지 확인
    verifyMeetingParticipant(findUser, findMeeting);

    // 주문 취소 상태가 아닌지 확인
    verifyPurchaseCancel(findPurchase);

    // 해당 모임이 함께 배달 모임인지 확인
    verifyDeliveryTogether(findMeeting);

    // 주문 전 모임인지 확인
    verifyBeforeOrder(findMeeting);

    return mapToIndividualResponse(
        individualPurchaseRepository.findAllByPurchase(findPurchase, pageable));
  }

  /**
   * 주문 전 모임 배달비 조회
   */
  @Override
  public DeliveryFeeResponse getDeliveryFeeInfo(Long userId, Long meetingId) {

    User findUser = findUserById(userId);
    Meeting findMeeting = meetingRepository.findWithStoreById(meetingId)
        .orElseThrow(() -> new CustomException(MEETING_NOT_FOUND));

    // 모임이 주문 전 상태인지 확인
    verifyBeforeOrder(findMeeting);

    // 해당 모임의 참가자 인지 확인
    verifyMeetingParticipant(findUser, findMeeting);

    // 해당 모임의 상점의 배송비 가져옴
    Long deliveryPrice = findMeeting.getStore().getDeliveryPrice();

    // 개인 별 배송비 가져옴
    Long participantCount = purchaseRepository.countParticipantByMeeting(findMeeting);
    // 일의 자리는 버림
    Integer deliveryFee = (int) ((deliveryPrice / participantCount) / 10) * 10;

    return PurchaseDto.DeliveryFeeResponse.builder()
        .price(deliveryPrice)
        .fee(deliveryFee.longValue())
        .build();
  }

  private PurchaseResponse mapToIndividualResponse
      (Page<IndividualPurchase> individualPurchaseList) {

    Pageable pageable = individualPurchaseList.getPageable();

    Long totalPrice = getIndividualTotalPrice(individualPurchaseList.getContent());
    List<Item> itemList = getIndividualItemList(individualPurchaseList.getContent());

    return PurchaseResponse.builder()
        .totalFee(totalPrice)
        .items(new PageImpl<>(itemList, pageable, individualPurchaseList.getTotalElements()))
        .build();
  }

  private PurchaseResponse mapToTeamResponse(Page<TeamPurchase> teamPurchaseList) {

    Pageable pageable = teamPurchaseList.getPageable();

    Long totalPrice = getTeamTotalPrice(teamPurchaseList.getContent());
    List<Item> itemList = getTeamItemList(teamPurchaseList.getContent());

    return PurchaseResponse.builder()
        .totalFee(totalPrice)
        .items(new PageImpl<>(itemList, pageable, teamPurchaseList.getTotalElements()))
        .build();
  }

  private long getTeamTotalPrice(List<TeamPurchase> teamPurchaseList) {
    return teamPurchaseList.stream()
        .mapToLong(teampurchase -> teampurchase.getQuantity() * teampurchase.getMenu().getPrice())
        .sum();
  }

  private long getIndividualTotalPrice(List<IndividualPurchase> individualPurchaseList) {
    return individualPurchaseList.stream()
        .mapToLong(individualPurchase ->
            individualPurchase.getQuantity() * individualPurchase.getMenu().getPrice()
        )
        .sum();
  }

  private List<Item> getTeamItemList(List<TeamPurchase> teamPurchaseList) {
    return teamPurchaseList.stream().map(teamPurchase -> {
          Menu menu = teamPurchase.getMenu();
          return fromMenuEntity(teamPurchase, menu);
        })
        .toList();
  }

  private List<Item> getIndividualItemList(List<IndividualPurchase> individualPurchaseList) {
    return individualPurchaseList.stream().map(teamPurchase -> {
          Menu menu = teamPurchase.getMenu();
          return fromMenuEntity(teamPurchase, menu);
        })
        .toList();
  }

  private Item fromMenuEntity(TeamPurchase purchase, Menu menu) {
    return Item.builder()
        .purchaseId(purchase.getId())
        .menuId(menu.getId())
        .name(menu.getName())
        .image(menu.getImage())
        .description(menu.getDescription())
        .price(menu.getPrice())
        .quantity(purchase.getQuantity())
        .build();
  }

  private Item fromMenuEntity(IndividualPurchase purchase, Menu menu) {
    return Item.builder()
        .purchaseId(purchase.getId())
        .menuId(menu.getId())
        .name(menu.getName())
        .image(menu.getImage())
        .description(menu.getDescription())
        .price(menu.getPrice())
        .quantity(purchase.getQuantity())
        .build();
  }

  private void verifyDiningTogether(Meeting findMeeting) {
    if (findMeeting.getPurchaseType() != DINING_TOGETHER) {
      throw new CustomException(MEETING_TYPE_INVALID);
    }
  }

  private void verifyDeliveryTogether(Meeting findMeeting) {
    if (findMeeting.getPurchaseType() != DELIVERY_TOGETHER) {
      throw new CustomException(MEETING_TYPE_INVALID);
    }
  }

  private void verifyMeetingParticipant(User findUser, Meeting findMeeting) {
    if (!purchaseRepository.existsByMeetingAndUser(findMeeting, findUser)) {
      throw new CustomException(MEETING_PARTICIPANT_NOT_MATCH);
    }
  }

  private void verifyBeforeOrder(Meeting findMeeting) {
    if (findMeeting.getStatus() != GATHERING) {
      throw new CustomException(MEETING_STATUS_INVALID);
    }
  }

  private void verifyPurchaseCancel(Purchase findPurchase) {
    if (findPurchase.getStatus() == CANCEL) {
      throw new CustomException(PURCHASE_STATUS_CANCEL);
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

  private Purchase findPurchaseByUserAndMeeting(User user, Meeting meeting) {
    return purchaseRepository.findByMeetingAndUser(meeting, user)
        .orElseThrow(() -> new CustomException(PURCHASE_NOT_FOUND));
  }
}
