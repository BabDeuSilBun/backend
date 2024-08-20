package com.zerobase.babdeusilbun.service.impl;

import static com.zerobase.babdeusilbun.enums.MeetingStatus.*;
import static com.zerobase.babdeusilbun.enums.PurchaseStatus.*;
import static com.zerobase.babdeusilbun.exception.ErrorCode.*;
import static com.zerobase.babdeusilbun.dto.MeetingRequest.*;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Purchase;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.StoreImage;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.DeliveryAddressDto;
import com.zerobase.babdeusilbun.dto.MetAddressDto;
import com.zerobase.babdeusilbun.dto.StoreImageDto;
import com.zerobase.babdeusilbun.dto.MeetingDto;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.dto.MeetingRequest.Update;
import com.zerobase.babdeusilbun.scheduler.MeetingScheduler;
import com.zerobase.babdeusilbun.service.MeetingService;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.PurchaseRepository;
import com.zerobase.babdeusilbun.repository.StoreImageRepository;
import com.zerobase.babdeusilbun.repository.StoreRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

  private final MeetingRepository meetingRepository;
  private final StoreImageRepository storeImageRepository;
  private final UserRepository userRepository;
  private final StoreRepository storeRepository;
  private final PurchaseRepository purchaseRepository;
  private final MeetingScheduler meetingScheduler;


  @Override
  @Transactional(readOnly = true)
  public Page<MeetingDto> getAllMeetingDtoList(
      Long schoolId, String sortCriteria, String searchMenu,
      Long categoryFilter, Pageable pageable) {

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
  public void confirmParticipant(Long userId, Long meetingId, Long purchaseId) {
    User findUser = findUserById(userId);
    Meeting findMeeting = findMeetingById(meetingId);
    Purchase findPurchase = findPurchaseById(purchaseId);

    // 모임이 주문 전 상태인지 확인
    verifyMeetingIsGathering(findMeeting);

    // 주문이 주문 전 상태인지 확인

    // 모임장인지 모임원인지 확인
    // 1. 모임장일 경우

    // 2. 모임원일 경우



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

  private Purchase findPurchaseById(Long purchaseId) {
    return purchaseRepository.findById(purchaseId)
        .orElseThrow(() -> new CustomException(PURCHASE_NOT_FOUND));
  }
}
