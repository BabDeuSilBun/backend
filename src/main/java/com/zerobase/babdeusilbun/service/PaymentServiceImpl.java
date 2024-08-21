package com.zerobase.babdeusilbun.service;

import static com.zerobase.babdeusilbun.enums.MeetingStatus.GATHERING;
import static com.zerobase.babdeusilbun.enums.PurchaseType.*;
import static com.zerobase.babdeusilbun.exception.ErrorCode.MEETING_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.MEETING_PARTICIPANT_NOT_MATCH;
import static com.zerobase.babdeusilbun.exception.ErrorCode.MEETING_STATUS_INVALID;
import static com.zerobase.babdeusilbun.exception.ErrorCode.PURCHASE_MEETING_NOT_MATCH;
import static com.zerobase.babdeusilbun.exception.ErrorCode.PURCHASE_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.PURCHASE_STATUS_INVALID;
import static com.zerobase.babdeusilbun.exception.ErrorCode.STORE_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.USER_NOT_FOUND;

import com.siot.IamportRestClient.IamportClient;
import com.zerobase.babdeusilbun.domain.IndividualPurchase;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Purchase;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.TeamPurchase;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.IamPortDto.Request;
import com.zerobase.babdeusilbun.dto.IamPortDto.Response;
import com.zerobase.babdeusilbun.enums.PurchaseStatus;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.repository.IndividualPurchaseRepository;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.PurchaseRepository;
import com.zerobase.babdeusilbun.repository.StoreRepository;
import com.zerobase.babdeusilbun.repository.TeamPurchaseRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

  private final UserRepository userRepository;
  private final StoreRepository storeRepository;
  private final MeetingRepository meetingRepository;
  private final PurchaseRepository purchaseRepository;
  private final TeamPurchaseRepository teamPurchaseRepository;

  private final IamportClient iamportClient;
  private final IndividualPurchaseRepository individualPurchaseRepository;

  @Override
  public Response confirmParticipant(Long userId, Long meetingId, Long purchaseId,
      Request request) {
    User findUser = findUserById(userId);
    Meeting findMeeting = findMeetingWithStoreById(meetingId);
    Purchase findPurchase = findPurchaseById(purchaseId);

    // 해당 모임 참가자 인지 확인
    verifyMeetingParticipant(findMeeting, findUser);

    // 모임이 주문 전 상태인지 확인
    verifyMeetingIsGathering(findMeeting);
//      IamportResponse<Payment> paymentIamportResponse = iamportClient.paymentByImpUid("sdf");

    // 주문이 주문 전 상태인지 확인
    verifyBeforePurchase(findPurchase);

    // 헤당 주문과 해당 모임이 올바른 관계인지 확인
    verifyMatching(findPurchase, findMeeting);

    String name;
    Integer price;

    // 최소 참가자 수 조회
    Long participantCount = findMeeting.getMinHeadcount().longValue();

    // 총 배송비
    Long deliveryPrice = findMeeting.getStore().getDeliveryPrice();

    // 공동 주문인지 개별 주문인지 확인
    // 1. 공동 주문일 겨우
    if (findPurchase.getMeeting().getPurchaseType() == DINING_TOGETHER) {

      // 공통 주문 메뉴들 조회
      List<TeamPurchase> purchaseList = teamPurchaseRepository.findAllByMeeting(findMeeting);

      // 금액 계산 (포인트 금액 차감)
      // 상품 총 금액
      Long totalPrice = purchaseList.stream()
          .mapToLong(tp -> tp.getQuantity() * tp.getMenu().getPrice())
          .sum();

      // 인원 수에 맞춰 계산
      price = (int) (((totalPrice + deliveryPrice) / participantCount) / 10) * 10
          - request.getPoint().intValue();

      // 상품명 생성
      name = getPaymentName(
          purchaseList.getFirst().getMenu().getName(), purchaseList.size()
      );
    }

    // 2. 개별 주문일 경우
    else {
      // 해당 유저의 개인 주문 상품들 조회
      List<IndividualPurchase> purchaseList =
          individualPurchaseRepository.findAllByPurchase(findPurchase);

      // 금액 계산 (포인트 금액 차감)
      Long totalPrice = purchaseList.stream()
          .mapToLong(ip -> ip.getQuantity() * ip.getMenu().getPrice())
          .sum();

      // 인원 수에 맞춰 계산
      price = (int) (((totalPrice + deliveryPrice) / participantCount) / 10) * 10
          - request.getPoint().intValue();

      // 상품명 생성
      name = getPaymentName(
          purchaseList.getFirst().getMenu().getName(), purchaseList.size()
      );
    }

    return Response.createNew(name, price);
  }

  private String getPaymentName(String firstItemName, int count) {
    if (count == 1) {
      return firstItemName;
    }
    return String.format("%s 외 %d건", firstItemName, count - 1);
  }

  private void verifyMatching(Purchase findPurchase, Meeting findMeeting) {
    if (findPurchase.getMeeting() != findMeeting) {
      throw new CustomException(PURCHASE_MEETING_NOT_MATCH);
    }
  }

  private void verifyBeforePurchase(Purchase findPurchase) {
    if (findPurchase.getStatus() != PurchaseStatus.PRE_PURCHASE) {
      throw new CustomException(PURCHASE_STATUS_INVALID);
    }
  }

  private void verifyMeetingParticipant(Meeting findMeeting, User findUser) {
    if (!purchaseRepository.existsByMeetingAndUser(findMeeting, findUser)) {
      throw new CustomException(MEETING_PARTICIPANT_NOT_MATCH);
    }
  }

  private void verifyMeetingIsGathering(Meeting findMeeting) {
    if (findMeeting.getStatus() != GATHERING) {
      throw new CustomException(MEETING_STATUS_INVALID);
    }
  }

  private User findUserById(Long userId) {
    return userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
  }

  private Store findStoreById(Long storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));
  }

  private Meeting findMeetingWithStoreById(Long meetingId) {
    return meetingRepository.findWithStoreById(meetingId)
        .orElseThrow(() -> new CustomException(MEETING_NOT_FOUND));
  }

  private Purchase findPurchaseById(Long purchaseId) {
    return purchaseRepository.findById(purchaseId)
        .orElseThrow(() -> new CustomException(PURCHASE_NOT_FOUND));
  }

}
