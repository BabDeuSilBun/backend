package com.zerobase.babdeusilbun.service.impl;

import static com.zerobase.babdeusilbun.dto.PurchaseDto.*;
import static com.zerobase.babdeusilbun.enums.MeetingStatus.*;
import static com.zerobase.babdeusilbun.exception.ErrorCode.*;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Menu;
import com.zerobase.babdeusilbun.domain.TeamPurchase;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.PurchaseDto;
import com.zerobase.babdeusilbun.dto.PurchaseDto.TeamPurchaseResponse;
import com.zerobase.babdeusilbun.dto.PurchaseDto.TeamPurchaseResponse.Item;
import com.zerobase.babdeusilbun.enums.MeetingStatus;
import com.zerobase.babdeusilbun.enums.PurchaseType;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.PurchaseRepository;
import com.zerobase.babdeusilbun.repository.TeamPurchaseRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.service.PurchaseService;
import java.util.List;
import java.util.Optional;
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

  /**
   * 주문 전 공동 주문 장바구니 조회
   */
  @Override
  public TeamPurchaseResponse getTeamOrderCart(Long userId, Long meetingId, Pageable pageable) {

    User findUser = findUserById(userId);
    Meeting findMeeting = findMeetingById(meetingId);

    // 해당 모임의 참가자 인지 확인
    verifyMeetingParticipant(findUser, findMeeting);

    // 해당 모임이 함께 식사 모임인지 확인
    verifyDiningTogether(findMeeting);

    // 주문 전 모임인지 확인
    if (findMeeting.getStatus() != GATHERING) {
      throw new CustomException(MEETING_STATUS_INVALID);
    }

    return mapToPurchaseResponse(teamPurchaseRepository.findAllByMeeting(findMeeting, pageable));
  }

  private TeamPurchaseResponse mapToPurchaseResponse(Page<TeamPurchase> teamPurchaseList) {

    Pageable pageable = teamPurchaseList.getPageable();

    Long totalPrice = getTeamTotalPrice(teamPurchaseList.getContent());
    List<Item> itemList = getResponseItemList(teamPurchaseList.getContent());

    return TeamPurchaseResponse.builder()
        .totalFee(totalPrice)
        .items(new PageImpl<>(itemList, pageable, teamPurchaseList.getTotalElements()))
        .build();
  }

  private long getTeamTotalPrice(List<TeamPurchase> teamPurchaseList) {
    return teamPurchaseList.stream()
        .mapToLong(teampurchase -> teampurchase.getQuantity() * teampurchase.getMenu().getPrice())
        .sum();
  }

  private List<Item> getResponseItemList(List<TeamPurchase> teamPurchaseList) {
    return teamPurchaseList.stream().map(teamPurchase -> {
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

  private void verifyDiningTogether(Meeting findMeeting) {
    if (findMeeting.getPurchaseType() == PurchaseType.DINING_TOGETHER) {
      throw new CustomException(MEETING_TYPE_INVALID);
    }
  }

  private void verifyMeetingParticipant(User findUser, Meeting findMeeting) {
    if (!purchaseRepository.existsByUserAndMeeting(findUser, findMeeting)) {
      throw new CustomException(MEETING_PARTICIPANT_NOT_MATCH);
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
