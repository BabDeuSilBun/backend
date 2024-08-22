package com.zerobase.babdeusilbun.service;

import static com.zerobase.babdeusilbun.enums.MeetingStatus.*;
import static com.zerobase.babdeusilbun.enums.PaymentStatus.*;
import static com.zerobase.babdeusilbun.enums.PointType.*;
import static com.zerobase.babdeusilbun.enums.PurchaseType.*;
import static com.zerobase.babdeusilbun.exception.ErrorCode.*;
import static com.zerobase.babdeusilbun.util.RedissonLockUtil.*;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.zerobase.babdeusilbun.domain.IndividualPurchase;
import com.zerobase.babdeusilbun.domain.IndividualPurchasePayment;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Menu;
import com.zerobase.babdeusilbun.domain.Payment;
import com.zerobase.babdeusilbun.domain.Point;
import com.zerobase.babdeusilbun.domain.Purchase;
import com.zerobase.babdeusilbun.domain.PurchasePayment;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.TeamPurchase;
import com.zerobase.babdeusilbun.domain.TeamPurchasePayment;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.PaymentDto.ConfirmRequest;
import com.zerobase.babdeusilbun.dto.PaymentDto.ConfirmResponse;
import com.zerobase.babdeusilbun.dto.PaymentDto.ProcessRequest;
import com.zerobase.babdeusilbun.dto.PaymentDto.ProcessResponse;
import com.zerobase.babdeusilbun.dto.PaymentDto.Temporary;
import com.zerobase.babdeusilbun.enums.PaymentGateway;
import com.zerobase.babdeusilbun.enums.PaymentMethod;
import com.zerobase.babdeusilbun.enums.PaymentStatus;
import com.zerobase.babdeusilbun.enums.PurchaseStatus;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.repository.IndividualPurchasePaymentRepository;
import com.zerobase.babdeusilbun.repository.IndividualPurchaseRepository;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.PaymentRepository;
import com.zerobase.babdeusilbun.repository.PointRepository;
import com.zerobase.babdeusilbun.repository.PurchasePaymentRepository;
import com.zerobase.babdeusilbun.repository.PurchaseRepository;
import com.zerobase.babdeusilbun.repository.TeamPurchasePaymentRepository;
import com.zerobase.babdeusilbun.repository.TeamPurchaseRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

  private final UserRepository userRepository;
  private final MeetingRepository meetingRepository;
  private final PurchaseRepository purchaseRepository;
  private final TeamPurchaseRepository teamPurchaseRepository;
  private final IndividualPurchaseRepository individualPurchaseRepository;
  private final PaymentRepository paymentRepository;
  private final TeamPurchasePaymentRepository teamPurchasePaymentRepository;
  private final PurchasePaymentRepository purchasePaymentRepository;
  private final IndividualPurchasePaymentRepository individualPurchasePaymentRepository;

  private final IamportClient iamportClient;
  private final PointRepository pointRepository;
  private final RedissonClient redissonClient;

  @Override
  public ProcessResponse requestPayment
      (Long userId, Long meetingId, Long purchaseId, ProcessRequest request) {
    User findUser = findUserById(userId);
    Meeting findMeeting = findMeetingWithStoreById(meetingId);
    Purchase findPurchase = findPurchaseById(purchaseId);

    // 해당 모임 참가자 인지 확인
    verifyMeetingParticipant(findMeeting, findUser);

    // 헤당 주문과 해당 모임이 올바른 관계인지 확인
    verifyMatching(findPurchase, findMeeting);

    // 모임이 주문 전 상태인지 확인
    verifyMeetingIsGathering(findMeeting);

    // 주문이 주문 전 상태인지 확인
    verifyBeforePurchase(findPurchase);

    // 포인트 사용 가능량 확인
    // 락 걸어놈
    RLock lock = redissonClient.getLock(getPointLockKey(userId));

    try {
      boolean isLocked = lock.tryLock(10, 10, TimeUnit.SECONDS);
      verifyLockTimeout(isLocked);

      Long requestPoint = request.getPoint();

      // 현재 포인트가 충분히 있는지 확인
      verifyPointSufficient(findUser.getPoint(), requestPoint);

      findUser.minusPoint(requestPoint);

    } catch (InterruptedException e) {
      throw new CustomException(REDISSON_LOCK_FAIL_OBTAIN);

    } finally {
      lock.unlock();
    }

    Long totalPrice;
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

      // 금액 계산 시작
      // 총 금액 계산
      totalPrice = purchaseList.stream()
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

      // 금액 계산 시작 (포인트 금액 차감)
      // 총 금액 계산
      totalPrice = purchaseList.stream()
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

    return ProcessResponse.createNew(name, price);
  }

  private void verifyPointSufficient(Long userPoint, Long requestPoint) {
    if (userPoint < requestPoint) {
      throw new CustomException(POINT_SHORTAGE);
    }
  }

  private void verifyLockTimeout(boolean isLocked) {
    if (!isLocked) {
      throw new CustomException(REDISSON_LOCK_TIMEOUT);
    }
  }

  @Override
  public ConfirmResponse confirmPayment
      (Long userId, Long meetingId, Long purchaseId,
          ConfirmRequest request, Temporary temporary) {

    User findUser = findUserById(userId);
    Meeting findMeeting = findMeetingWithStoreById(meetingId);
    Purchase findPurchase = findPurchaseById(purchaseId);

    // 해당 모임 참가자 인지 확인
    verifyMeetingParticipant(findMeeting, findUser);

    // 모임이 주문 전 상태인지 확인
    verifyMeetingIsGathering(findMeeting);

    // 주문이 주문 전 상태인지 확인
    verifyBeforePurchase(findPurchase);

    // 헤당 주문과 해당 모임이 올바른 관계인지 확인
    verifyMatching(findPurchase, findMeeting);

    // 결제의 트랜젝션 id가 같은지 확인
    verifyTransactionId(request, temporary);

    com.siot.IamportRestClient.response.Payment payment =
        getIamportPayment(request.getPortoneUid());

    try {
      // 결제정보가 올바른지 확인
      verifyPaymentInformation(temporary, payment);
    } catch (CustomException e) {
      log.error(e.getMessage());

      // 포인트 회복
      return ConfirmResponse.createWhenFail(request.getTransactionId());
    }

    PaymentStatus status = fromCode(payment.getStatus());

    // 주문 상태 변경
    switch (status) {
      case READY, FAILED -> findPurchase.failPayment();
      case PAID -> findPurchase.successPayment();
    }

    // status가 실패일 경우 포인트 복구 & 결제 진행 멈추고 프론트로 반환
    if (status != PAID) {

      findUser.plusPoint(temporary.getPoint());

      return ConfirmResponse.createWhenSuccess(request.getTransactionId());
    }

    // 공동주문 or 개인주문 스냅샷 생성
    PurchasePayment purchasePayment;
    boolean isTeam = findMeeting.getPurchaseType() == DINING_TOGETHER;
    Store store = findMeeting.getStore();
    Long deliveryPrice = store.getDeliveryPrice();
    Long deliveryFee = deliveryPrice / findMeeting.getMinHeadcount();

    // 공동주문일 경우
    if (isTeam) {
      List<TeamPurchase> teamPurchaseList = teamPurchaseRepository.findAllByMeeting(findMeeting);

      List<TeamPurchasePayment> teamPurchasePaymentList = teamPurchaseList
          .stream().map(tp -> {
            Menu menu = tp.getMenu();
            return TeamPurchasePayment.builder()
                .teamPurchase(tp)
                .menuId(menu.getId())
                .menuName(menu.getName())
                .image(menu.getImage())
                .menuDescription(menu.getDescription())
                .menuPrice(menu.getPrice())
                .quantity(tp.getQuantity())
                .build();
          }).toList();
      teamPurchasePaymentRepository.saveAll(teamPurchasePaymentList);

      Long teamPurchasePrice =
          teamPurchaseList.stream()
              .mapToLong(tp -> tp.getMenu().getPrice() * tp.getQuantity())
              .sum();
      Long teamPurchaseFee = teamPurchasePrice / findMeeting.getMinHeadcount();

      // 주문 스냅샷 생성
      PurchasePayment createdPurchasePayment = PurchasePayment.builder()
          .purchase(findPurchase)
          .deliveryPrice(deliveryPrice)
          .deliveryFee(deliveryFee)
          .teamPurchasePrice(teamPurchasePrice)
          .teamPurchaseFee(teamPurchaseFee)
          .point(temporary.getPoint())
          .build();

      purchasePayment = purchasePaymentRepository.save(createdPurchasePayment);
    }
    // 개인 주문일 경우
    else {
      List<IndividualPurchase> individualPurchaseList =
          individualPurchaseRepository.findAllByPurchase(findPurchase);

      List<IndividualPurchasePayment> individualPurchasePaymentList =
          individualPurchaseList.stream().map(ip -> {
            Menu menu = ip.getMenu();
            return IndividualPurchasePayment.builder()
                .individualPurchase(ip)
                .menuId(menu.getId())
                .menuName(menu.getName())
                .image(menu.getImage())
                .menuDescription(menu.getDescription())
                .menuPrice(menu.getPrice())
                .quantity(ip.getQuantity())
                .build();
          }).toList();

      individualPurchasePaymentRepository.saveAll(individualPurchasePaymentList);

      Long individualPurchasePrice = individualPurchaseList.stream()
          .mapToLong(ip -> ip.getQuantity() * ip.getMenu().getPrice())
          .sum();

      // 주문 스냅샷 생성
      PurchasePayment createdPurchasePayment = PurchasePayment.builder()
          .purchase(findPurchase)
          .deliveryPrice(deliveryPrice)
          .deliveryFee(deliveryFee)
          .individualPurchasePrice(individualPurchasePrice)
          .build();
      purchasePayment = purchasePaymentRepository.save(createdPurchasePayment);
    }

    // 결제 스냅샷 생성
    Payment createdPayment =
        createPaymentEntity(findPurchase, temporary, request.getPortoneUid(), status);
    paymentRepository.save(createdPayment);

    // 포인트 소모
    // 포인트 객체 생성
    Point createdPoint = Point.builder()
        .user(findUser).purchasePayment(purchasePayment)
        .type(MINUS).amount(temporary.getPoint())
        .content(temporary.getName())
        .build();
    Point savedPoint = pointRepository.save(createdPoint);

    // 사용자 정보에서 포인트 감소
    findUser.minusPoint(savedPoint.getAmount());

    return ConfirmResponse.createWhenSuccess(request.getTransactionId());
  }

  private Payment
  createPaymentEntity
      (Purchase purchase, Temporary temporary, String portoneUid, PaymentStatus status) {

    return Payment.builder()
        .purchase(purchase)
        .transactionId(temporary.getTransactionId())
        .portoneUid(portoneUid)
        .amount(temporary.getPrice())
        .pg(temporary.getPg())
        .method(temporary.getPayMethod())
        .status(status)
        .build();
  }

  private com.siot.IamportRestClient.response.Payment getIamportPayment(String portoneId) {
    try {
      IamportResponse<com.siot.IamportRestClient.response.Payment> response =
          iamportClient.paymentByImpUid(portoneId);

      verifyIamportResponseCode(response);

      return response.getResponse();

    } catch (IamportResponseException | IOException e) {
      throw new CustomException(IAMPORT_SERVER_ERROR);
    }
  }

  private String getPaymentName(String firstItemName, int count) {
    if (count == 1) {
      return firstItemName;
    }
    // 결제 대상이 되는 상품이 여러개일 경우
    return String.format("%s 외 %d건", firstItemName, count - 1);
  }

  private void verifyPaymentInformation(
      Temporary temporary,
      com.siot.IamportRestClient.response.Payment payment) {

    boolean name = temporary.getName().equals(payment.getName());
    boolean price = temporary.getPrice() == payment.getAmount().longValueExact();
    boolean pg = temporary.getPg() == PaymentGateway.fromCode(payment.getPgProvider());
    boolean pm = temporary.getPayMethod() == PaymentMethod.fromCode(payment.getPayMethod());

    if (!name || !price || !pg || !pm) {
      throw new CustomException(PAYMENT_INFORMATION_NOT_MATCH);
    }
  }

  private void verifyIamportResponseCode
      (IamportResponse<com.siot.IamportRestClient.response.Payment> response) {
    if (response.getCode() != 1) {
      throw new CustomException(IAMPORT_SERVER_ERROR);
    }
  }

  private void verifyTransactionId(ConfirmRequest request, Temporary temporary) {
    if (!temporary.getTransactionId().equals(request.getTransactionId())) {
      throw new CustomException(PAYMENT_INFORMATION_NOT_MATCH);
    }
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

  // fetch join으로 store 정보도 같이 가져옴
  private Meeting findMeetingWithStoreById(Long meetingId) {
    return meetingRepository.findWithStoreById(meetingId)
        .orElseThrow(() -> new CustomException(MEETING_NOT_FOUND));
  }

  private Purchase findPurchaseById(Long purchaseId) {
    return purchaseRepository.findById(purchaseId)
        .orElseThrow(() -> new CustomException(PURCHASE_NOT_FOUND));
  }

}
