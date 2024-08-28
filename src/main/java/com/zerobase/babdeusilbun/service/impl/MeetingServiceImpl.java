package com.zerobase.babdeusilbun.service.impl;

import static com.zerobase.babdeusilbun.dto.MeetingRequest.Create;
import static com.zerobase.babdeusilbun.enums.MeetingStatus.GATHERING;
import static com.zerobase.babdeusilbun.enums.PointType.PLUS;
import static com.zerobase.babdeusilbun.enums.PurchaseStatus.PRE_PURCHASE;
import static com.zerobase.babdeusilbun.enums.UserAlarmType.COOKING_COMPLETED;
import static com.zerobase.babdeusilbun.enums.UserAlarmType.ORDER_APPROVED;
import static com.zerobase.babdeusilbun.enums.UserAlarmType.ORDER_DELAY;
import static com.zerobase.babdeusilbun.enums.UserAlarmType.ORDER_REJECTED;
import static com.zerobase.babdeusilbun.enums.UserAlarmType.POINT_REFUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.CHATROOM_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.ENTREPRENEUR_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.MEETING_LEADER_NOT_MATCH;
import static com.zerobase.babdeusilbun.exception.ErrorCode.MEETING_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.MEETING_PARTICIPANT_EXIST;
import static com.zerobase.babdeusilbun.exception.ErrorCode.MEETING_PURCHASE_TIME_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.MEETING_STATUS_INVALID;
import static com.zerobase.babdeusilbun.exception.ErrorCode.NO_AUTH_ON_PURCHASE;
import static com.zerobase.babdeusilbun.exception.ErrorCode.NO_AUTH_ON_STORE;
import static com.zerobase.babdeusilbun.exception.ErrorCode.PURCHASE_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.PURCHASE_PAYMENT_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.STORE_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.USER_NOT_FOUND;
import static com.zerobase.babdeusilbun.util.MeetingUtility.CANCELED_STATUS;
import static com.zerobase.babdeusilbun.util.MeetingUtility.CAN_ENTREPRENEUR_CHECK_PURCHASE_STATUS;
import static com.zerobase.babdeusilbun.util.MeetingUtility.ENTREPRENEUR_CAN_COMPLETE;
import static com.zerobase.babdeusilbun.util.MeetingUtility.ENTREPRENEUR_CAN_CONFIRM_OR_DENY;
import static com.zerobase.babdeusilbun.util.MeetingUtility.ENTREPRENEUR_CAN_SEND_DELAY_MESSAGE;
import static com.zerobase.babdeusilbun.util.MeetingUtility.getTitle;

import com.zerobase.babdeusilbun.domain.ChatRoom;
import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.MeetingPurchaseTime;
import com.zerobase.babdeusilbun.domain.Point;
import com.zerobase.babdeusilbun.domain.Purchase;
import com.zerobase.babdeusilbun.domain.PurchasePayment;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.StoreImage;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.domain.UserAlarm;
import com.zerobase.babdeusilbun.dto.ChatDto.Request;
import com.zerobase.babdeusilbun.dto.DeliveryAddressDto;
import com.zerobase.babdeusilbun.dto.MeetingDto;
import com.zerobase.babdeusilbun.dto.MeetingRequest.Update;
import com.zerobase.babdeusilbun.dto.MetAddressDto;
import com.zerobase.babdeusilbun.dto.PurchaseDto.MenuResponse;
import com.zerobase.babdeusilbun.dto.StoreImageDto;
import com.zerobase.babdeusilbun.enums.PurchaseStatus;
import com.zerobase.babdeusilbun.enums.UserAlarmType;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.repository.ChatRoomRepository;
import com.zerobase.babdeusilbun.repository.EntrepreneurRepository;
import com.zerobase.babdeusilbun.repository.MeetingPurchaseTimeRepository;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.PointRepository;
import com.zerobase.babdeusilbun.repository.PurchasePaymentRepository;
import com.zerobase.babdeusilbun.repository.PurchaseRepository;
import com.zerobase.babdeusilbun.repository.StoreImageRepository;
import com.zerobase.babdeusilbun.repository.StoreRepository;
import com.zerobase.babdeusilbun.repository.UserAlarmRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.scheduler.MeetingScheduler;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.MeetingService;
import io.micrometer.common.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

  private final MeetingRepository meetingRepository;
  private final MeetingPurchaseTimeRepository meetingPurchaseTimeRepository;
  private final StoreImageRepository storeImageRepository;
  private final UserRepository userRepository;
  private final EntrepreneurRepository entrepreneurRepository;
  private final StoreRepository storeRepository;
  private final PurchaseRepository purchaseRepository;
  private final PurchasePaymentRepository purchasePaymentRepository;
  private final MeetingScheduler meetingScheduler;
  private final ChatRoomRepository chatRoomRepository;
  private final PointRepository pointRepository;
  private final UserAlarmRepository userAlarmRepository;

  private final ChatServiceImpl chatService;

  @Override
  @Transactional(readOnly = true)
  public Page<MeetingDto> getAllMeetingDtoList(
      Long userId, Long schoolId, String sortCriteria, String searchMenu,
      Long categoryFilter, Pageable pageable) {

    if (schoolId == null || schoolId == 0L) {
      schoolId = userRepository.findByIdAndDeletedAtIsNull(userId)
          .orElseThrow(() -> new CustomException(USER_NOT_FOUND))
          .getSchool().getId();
    }

    return getAllMeetingList(schoolId, sortCriteria, searchMenu, categoryFilter, pageable)
        .map(this::mapToMeetingDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Meeting> getAllMeetingList
      (Long schoolId, String sortCriteria, String searchMenu,
      Long categoryFilter, Pageable pageable) {

    return meetingRepository
        .findFilteredMeetingList(schoolId, sortCriteria, searchMenu, categoryFilter, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public MeetingDto getMeetingInfoDto(Long meetingId) {
    return mapToMeetingDto(getMeetingInfo(meetingId));
  }

  @Override
  @Transactional(readOnly = true)
  public Meeting getMeetingInfo(Long meetingId) {
    return findMeetingById(meetingId);
  }

  @Override
  public void createMeeting(Create request, CustomUserDetails userDetails) {

    User findUser = getUserFromUserDetails(userDetails);

    Meeting meetingFromRequest = createMeetingFromRequest(request, findUser);
    Meeting savedMeeting = meetingRepository.save(meetingFromRequest);

    // 주문 생성
    Purchase createdPurchase = Purchase.builder()
        .meeting(savedMeeting).user(findUser).status(PRE_PURCHASE).build();
    purchaseRepository.save(createdPurchase);

    // 모임 마감 시간 등록
    meetingScheduler.enrollMeetingSchedule(savedMeeting);
  }

  @Override
  public void updateMeeting(Long meetingId, Update request, CustomUserDetails userDetails) {
    User findUser = getUserFromUserDetails(userDetails);
    Meeting findMeeting = findMeetingById(meetingId);

    // 해당 모임의 leader 인지 확인
    verifyMeetingLeader(findUser, findMeeting);
    // 해당 모임의 상태가 업데이트 가능 상태인지 확인
    verifyMeetingIsGathering(findMeeting);

    findMeeting.updateFromRequest(request);
  }

  @Override
  public void withdrawMeeting(Long meetingId, CustomUserDetails userDetails) {
    // 탈퇴 취소는 주문 전이어야 함
    // 리더인 경우 현재 참여한 모임원이 없어야 함
    User findUser = getUserFromUserDetails(userDetails);
    Meeting findMeeting = findMeetingById(meetingId);
    ChatRoom findChatRoom = findChatRoomByMeeting(findMeeting);

    // 1. 모임이 주문 전 상태인지 확인 (GATHERING)
    verifyMeetingIsGathering(findMeeting);

    // 2. 모임원인지 모임장인지 판별
    // - 모임장인경우
    if (findMeeting.getLeader() == findUser) {
      // 해당 모임에 참가자가 본인밖에 없는지 확인 (주문 갯수 조회)
      // - 다른 참가자가 있을 경우 예외 발생
      verifyExistParticipant(findMeeting);

      // 해당 모임에 관련된 주문 취소
      purchaseRepository.findAllByMeeting(findMeeting)
          .forEach(Purchase::cancel);

      // 해당 모임 delete 시간 추가
      // 모임 상태 MEETING_CANCELED로 변경
      findMeeting.delete();

      meetingScheduler.deleteMeetingSchedule(findMeeting);

      return;
    }

    // - 모임원인 경우

    // 해당 모임에 관련된 주문 취소
    Purchase findPurchase = purchaseRepository.findByMeetingAndUser(findMeeting, findUser)
        .orElseThrow(() -> new CustomException(PURCHASE_NOT_FOUND));
    findPurchase.cancel();
    
    //채팅방 탈퇴
    chatService.leaveChatRoom(findChatRoom, findUser);
  }

  @Override
  @Transactional(readOnly = true)
  public User getMeetingLeaderInfo(Long meetingId) {

    return findMeetingById(meetingId).getLeader();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<User> getMeetingParticipants(Long meetingId, Pageable pageable) {

    return userRepository
        .findAllMeetingParticipant(meetingId, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public int getMeetingHeadCount(Long meetingId) {
    return userRepository.countMeetingParticipant(meetingId).intValue();
  }

  @Override
  @Transactional
  public void confirmMeetingPurchase(Long entrepreneurId, Long meetingId) {
    Meeting meeting = meetingRepository
        .findByIdAndStatusInAndDeletedAtIsNull(meetingId, ENTREPRENEUR_CAN_CONFIRM_OR_DENY)
        .orElseThrow(() -> new CustomException(MEETING_NOT_FOUND));

    confirmEntrepreneurAuthOfPurchaseByMeeting(
        entrepreneurRepository.findByIdAndDeletedAtIsNull(entrepreneurId)
        .orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND)), meeting);

    MeetingPurchaseTime meetingPurchaseTime = meetingPurchaseTimeRepository.findByMeeting(meeting)
        .orElseGet(() -> createMeetingPurchaseTimeForMeeting(meeting, meeting.getStore()));

    //모임상태 변경
    meeting.confirmMeetingPurchase();
    
    //totalAmount 만큼 결제(밥드실분 -> 상점) : 진행되었다 가정
    Long totalAmount = getTotalPurchaseAmountOfMeeting(
        purchaseRepository.findAllByMeetingAndStatus(meeting, PurchaseStatus.PAYMENT_COMPLETED)
    );

    //시간 기록
    meetingPurchaseTime.writeProcessedAt();
  }

  @Override
  @Transactional
  public void denyMeetingPurchase(Long entrepreneurId, Long meetingId) {
    Meeting meeting = meetingRepository
        .findByIdAndStatusInAndDeletedAtIsNull(meetingId, ENTREPRENEUR_CAN_CONFIRM_OR_DENY)
        .orElseThrow(() -> new CustomException(MEETING_NOT_FOUND));

    confirmEntrepreneurAuthOfPurchaseByMeeting(
        entrepreneurRepository.findByIdAndDeletedAtIsNull(entrepreneurId)
            .orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND)), meeting);

    MeetingPurchaseTime meetingPurchaseTime = meetingPurchaseTimeRepository.findByMeeting(meeting)
        .orElseGet(() -> createMeetingPurchaseTimeForMeeting(meeting, meeting.getStore()));

    //모임상태 변경
    meeting.denyMeetingPurchase();

    //모임에 개인 주문 내역(주문 상태: 결제 완료) 확인
    purchaseRepository.findAllByMeetingAndStatus(meeting, PurchaseStatus.PAYMENT_COMPLETED)
        .forEach(purchase -> {
          PurchasePayment purchasePayment =
              purchasePaymentRepository.findByMeetingAndUser(purchase.getMeeting(), purchase.getUser())
                  .orElseThrow(() -> new CustomException(PURCHASE_PAYMENT_NOT_FOUND));

          //차액 환급
          Long refundPoint = getTotalIndividualPaymentAmount(purchasePayment);
          refundPointToUser(purchase.getUser(), purchasePayment, refundPoint);

          //알림 전송(멤버별 주문이 거절되었어요/ㅇㅇ포인트가 환급되었어요)
          userAlarmRepository.saveAll(List.of(
              purchaseStatusAlarm(purchase, ORDER_REJECTED), refundAlarmOfPurchase(purchase, refundPoint)
          ));

          //모임, 이용자 식별번호에 따른 주문 상태 모두 취소로 변경
          purchase.cancel();
        });

    //시간 기록
    meetingPurchaseTime.writeProcessedAt();
  }

  @Override
  @Transactional
  public void completeMeetingPurchase(Long entrepreneurId, Long meetingId) {
    Meeting meeting = meetingRepository
        .findByIdAndStatusInAndDeletedAtIsNull(meetingId, ENTREPRENEUR_CAN_COMPLETE)
        .orElseThrow(() -> new CustomException(MEETING_NOT_FOUND));

    confirmEntrepreneurAuthOfPurchaseByMeeting(
        entrepreneurRepository.findByIdAndDeletedAtIsNull(entrepreneurId)
            .orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND)), meeting);

    MeetingPurchaseTime meetingPurchaseTime = meetingPurchaseTimeRepository.findByMeeting(meeting)
        .orElseThrow(() -> new CustomException(MEETING_PURCHASE_TIME_NOT_FOUND));

    //모임상태 변경
    meeting.completedCooking();

    //모임에 개인 주문 내역(주문 상태: 결제 완료) 확인 후 알람 전송
    List<UserAlarm> alarms = purchaseRepository.findAllByMeetingAndStatus(meeting, PurchaseStatus.PAYMENT_COMPLETED)
        .stream().map(this::completedCookAlarmOfMeeting).toList();
    userAlarmRepository.saveAll(alarms);

    //시간 기록
    meetingPurchaseTime.writeCookedAt();
  }

  @Override
  @Transactional
  public void sendMessageForDelayMeetingPurchases(Long entrepreneurId, Long meetingId, Request request) {
    Meeting meeting = meetingRepository
        .findByIdAndStatusInAndDeletedAtIsNull(meetingId, ENTREPRENEUR_CAN_SEND_DELAY_MESSAGE)
        .orElseThrow(() -> new CustomException(MEETING_NOT_FOUND));

    confirmEntrepreneurAuthOfPurchaseByMeeting(
        entrepreneurRepository.findByIdAndDeletedAtIsNull(entrepreneurId)
            .orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND)), meeting);

    //모임에 개인 주문 내역(주문 상태: 결제 완료) 확인, 알람 메세지 전송(조리/배차가 지연되고 있어요.)
    List<UserAlarm> alarms = purchaseRepository.findAllByMeetingAndStatus(meeting, PurchaseStatus.PAYMENT_COMPLETED)
        .stream().map(purchase -> delayOfMeeting(purchase, request.getContent())).toList();
    userAlarmRepository.saveAll(alarms);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<MenuResponse> getMeetingPurchaseByStoreIdAndMeetingId(Long entrepreneurId, Long storeId, Long meetingId,
      int page, int size) {
    Meeting meeting = meetingRepository.findById(meetingId)
        .orElseThrow(() -> new CustomException(MEETING_NOT_FOUND));

    if (!Objects.equals(meeting.getStore().getId(), storeId)
        || !Objects.equals(meeting.getStore().getEntrepreneur().getId(), entrepreneurId)) {
      throw new CustomException(NO_AUTH_ON_STORE);
    }

    if (CAN_ENTREPRENEUR_CHECK_PURCHASE_STATUS.contains(meeting.getStatus())) {
      return getMenuByMeetingAndStatus(meeting, page, size);
    } else {
      throw new CustomException(NO_AUTH_ON_PURCHASE); //주문 내역을 볼 수 있는 권한이 있는 상태가 아님
    }
  }

  public Page<MenuResponse> getMenuByMeetingAndStatus(Meeting meeting, int page, int size) {
    PurchaseStatus status = (CANCELED_STATUS.contains(meeting.getStatus())) ?
        PurchaseStatus.CANCEL : PurchaseStatus.PAYMENT_COMPLETED;

    int count = meetingRepository.countPurchaseMenuByMeetingAndStatus(meeting.getId(), status.name());
    if (count == 0) {
      return new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 1), 0);
    }

    size = (size <= 0) ? count : size;
    page = Math.min(page, ((int) Math.ceil((double) count / size))-1);

    Pageable pageable = PageRequest.of(page, size);

    return meetingRepository.findAllPurchaseMenuByMeetingAndStatus(meeting.getId(), status.name(), pageable);
  }

  private void verifyExistParticipant(Meeting findMeeting) {
    if (purchaseRepository.findAllByMeeting(findMeeting).size() != 1) {
      throw new CustomException(MEETING_PARTICIPANT_EXIST);
    }
  }

  private void verifyMeetingIsGathering(Meeting findMeeting) {
    if (findMeeting.getStatus() != GATHERING) {
      throw new CustomException(MEETING_STATUS_INVALID);
    }
  }

  private void verifyMeetingLeader(User findUser, Meeting findMeeting) {
    if (findUser != findMeeting.getLeader()) {
      throw new CustomException(MEETING_LEADER_NOT_MATCH);
    }
  }

  private MeetingDto mapToMeetingDto(Meeting meeting) {

    Store store = meeting.getStore();
    List<StoreImage> storeImageList =
        storeImageRepository.findAllByStoreOrderBySequenceAsc(store);

    return MeetingDto.builder()
        .meetingId(meeting.getId())
        .storeId(store.getId())
        .storeImage(storeImageList.stream().map(StoreImageDto::fromEntity).toList())
        .storeName(store.getName())
        .purchaseType(meeting.getPurchaseType())
        .participantMin(meeting.getMinHeadcount())
        .participantMax(meeting.getMaxHeadcount())
        .isEarlyPaymentAvailable(meeting.getIsEarlyPaymentAvailable())
        .paymentAvailableAt(meeting.getPaymentAvailableDt())
        .deliveryAddress(DeliveryAddressDto.fromEntity(meeting.getDeliveredAddress()))
        .metAddress(MetAddressDto.fromEntity(meeting.getMetAddress()))
        .deliveryFee(store.getDeliveryPrice())
        .deliveredAt(meeting.getDeliveredAt())
        .status(meeting.getStatus())
        .description(meeting.getDescription())
        .build();
  }

  private Meeting createMeetingFromRequest(Create request, User leader) {

    return Meeting.builder()
        .leader(leader)
        .store(findStoreById(request.getStoreId()))
        .purchaseType(request.getPurchaseType())
        .minHeadcount(request.getMinHeadcount())
        .maxHeadcount(request.getMaxHeadcount())
        .isEarlyPaymentAvailable(request.getIsEarlyPaymentAvailable())
        .paymentAvailableDt(request.getPaymentAvailableAt())
        .deliveredAddress(request.getDeliveryAddress().toAddressEntity())
        .metAddress(request.getMetAddress().toAddressEntity())
        .status(GATHERING)
        .build();
  }

  private User getUserFromUserDetails(CustomUserDetails userDetails) {
    return findUserById(userDetails.getId());
  }

  private User findUserById(Long userId) {
    return userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
  }

  private Store findStoreById(Long storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));
  }

  private Meeting findMeetingById(Long meetingId) {
    return meetingRepository.findById(meetingId)
        .orElseThrow(() -> new CustomException(MEETING_NOT_FOUND));
  }

  private ChatRoom findChatRoomByMeeting(Meeting meeting) {
    return chatRoomRepository.findByMeeting(meeting)
        .orElseThrow(() -> new CustomException(CHATROOM_NOT_FOUND));
  }

  private void confirmEntrepreneurAuthOfPurchaseByMeeting(Entrepreneur entrepreneur, Meeting meeting) {
    if (Objects.equals(entrepreneur, meeting.getStore().getEntrepreneur())) {
      throw new CustomException(NO_AUTH_ON_PURCHASE);
    }
  }

  private MeetingPurchaseTime createMeetingPurchaseTimeForMeeting(Meeting meeting, Store store) {
    return meetingPurchaseTimeRepository.save(
        MeetingPurchaseTime.builder()
            .meeting(meeting)
            .store(store)
            .build()
    );
  }

  private Long getRefundAmountWhenConfirmByPurchasePayment(PurchasePayment purchasePayment, int headCount) {
    return purchasePayment.getDeliveryFee() + purchasePayment.getTeamPurchaseFee()
        - (purchasePayment.getDeliveryPrice() + purchasePayment.getTeamPurchasePrice())/headCount;
  }

  private Long getTotalIndividualPaymentAmount(PurchasePayment purchasePayment) {
    return purchasePayment.getIndividualPurchasePrice()
        + purchasePayment.getDeliveryFee()
        + purchasePayment.getTeamPurchaseFee();
  }

  private void refundPointToUser(User user, PurchasePayment purchasePayment, Long point) {
    user.plusPoint(point);

    pointRepository.save(
        Point.builder()
            .user(user).purchasePayment(purchasePayment)
            .type(PLUS).amount(point)
            .content(PLUS.getContent())
            .build()
    );
  }

  private UserAlarm purchaseStatusAlarm(Purchase purchase, UserAlarmType type) {
    return UserAlarm.builder()
        .user(purchase.getUser())
        .type(type)
        .content(type.getMessage(getTitle(purchase.getMeeting())))
        .build();
  }

  private UserAlarm refundAlarmOfPurchase(Purchase purchase, Long refundPoint) {
    return UserAlarm.builder()
        .user(purchase.getUser())
        .type(POINT_REFUND)
        .content(POINT_REFUND.getMessage(getTitle(purchase.getMeeting()), refundPoint.toString()))
        .build();
  }

  private UserAlarm completedCookAlarmOfMeeting(Purchase purchase) {
    return UserAlarm.builder()
        .user(purchase.getUser())
        .type(COOKING_COMPLETED)
        .content(COOKING_COMPLETED.getMessage(getTitle(purchase.getMeeting())))
        .build();
  }

  private UserAlarm delayOfMeeting(Purchase purchase, String plusContent) {
    if (StringUtils.isBlank(plusContent)) {
      plusContent = "개인 사정";
    }

    return UserAlarm.builder()
        .user(purchase.getUser())
        .type(ORDER_DELAY)
        .content(ORDER_DELAY.getMessage(getTitle(purchase.getMeeting()), plusContent))
        .build();
  }

  private Long getTotalPurchaseAmountOfMeeting(List<Purchase> purchases) {
    //모임에서 가게로 결제해야 하는 금액
    AtomicLong totalAmount = new AtomicLong(0);

    //모임에 주문 내역(주문 상태: 결제 완료) 확인 > 참여자 별로 주문 승인 과정 시행
    purchases.forEach(purchase -> {
      PurchasePayment purchasePayment =
          purchasePaymentRepository.findByMeetingAndUser(purchase.getMeeting(), purchase.getUser())
              .orElseThrow(() -> new CustomException(PURCHASE_PAYMENT_NOT_FOUND));

      //차액 환급
      Long refundPoint = getRefundAmountWhenConfirmByPurchasePayment(purchasePayment, purchases.size());
      refundPointToUser(purchase.getUser(), purchasePayment, refundPoint);

      //알림 전송(멤버별 주문이 승인되었어요/ㅇㅇ포인트가 환급되었어요)
      userAlarmRepository.saveAll(List.of(
          purchaseStatusAlarm(purchase, ORDER_APPROVED), refundAlarmOfPurchase(purchase, refundPoint)
      ));

      if (totalAmount.get() == 0) {
        totalAmount.getAndAdd(purchasePayment.getDeliveryPrice() + purchasePayment.getTeamPurchasePrice());
      }

      totalAmount.getAndAdd(purchasePayment.getIndividualPurchasePrice());
    });

    return totalAmount.get();
  }
}
