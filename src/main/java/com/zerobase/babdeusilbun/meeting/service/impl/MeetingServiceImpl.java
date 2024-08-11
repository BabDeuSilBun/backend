package com.zerobase.babdeusilbun.meeting.service.impl;

import static com.zerobase.babdeusilbun.enums.MeetingStatus.*;
import static com.zerobase.babdeusilbun.exception.ErrorCode.*;
import static com.zerobase.babdeusilbun.meeting.dto.MeetingRequest.*;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.StoreImage;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.DeliveryAddressDto;
import com.zerobase.babdeusilbun.dto.MetAddressDto;
import com.zerobase.babdeusilbun.dto.StoreImageDto;
import com.zerobase.babdeusilbun.dto.MeetingDto;
import com.zerobase.babdeusilbun.enums.MeetingStatus;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.meeting.dto.MeetingRequest.Update;
import com.zerobase.babdeusilbun.meeting.service.MeetingService;
import com.zerobase.babdeusilbun.repository.MeetingQueryRepository;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.StoreImageRepository;
import com.zerobase.babdeusilbun.repository.StoreRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

  private final MeetingRepository meetingRepository;
  private final MeetingQueryRepository meetingQueryRepository;
  private final StoreImageRepository storeImageRepository;
  private final UserRepository userRepository;
  private final StoreRepository storeRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<MeetingDto> getAllMeetingList(Long schoolId, String sortCriteria, String searchMenu,
      Pageable pageable) {

    return meetingQueryRepository
        .findFilteredMeetingList(schoolId, sortCriteria, searchMenu, pageable)
        .map(this::mapToMeetingDto);
  }

  @Override
  @Transactional(readOnly = true)
  public MeetingDto getMeetingInfo(Long meetingId) {
    return mapToMeetingDto(findMeetingById(meetingId));
  }

  @Override
  public void createMeeting(Create request, UserDetails userDetails) {

    User findUser = getUserFromUserDetails(userDetails);

    Meeting meetingFromRequest = createMeetingFromRequest(request, findUser);
    meetingRepository.save(meetingFromRequest);
  }

  @Override
  public void updateMeeting(Long meetingId, Update request, UserDetails userDetails) {
    User findUser = getUserFromUserDetails(userDetails);
    Meeting findMeeting = findMeetingById(meetingId);

    // 해당 모임의 leader 인지 확인
    verifyMeetingLeader(findUser, findMeeting);
    // 해당 모임의 상태가 업데이트 가능 상태인지 확인
    verifyMeetingStatus(findMeeting, GATHERING);

    findMeeting.updateFromRequest(request);
  }

  private void verifyMeetingStatus(Meeting findMeeting, MeetingStatus status) {
    if (findMeeting.getStatus() != status) {
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

  private User getUserFromUserDetails(UserDetails userDetails) {
    String emailByUserDetails = userDetails.getUsername();
    return findUserByEmail(emailByUserDetails);
  }

  private User findUserByEmail(String emailByUserDetails) {
    return userRepository.findByEmail(emailByUserDetails)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
  }

  private Store findStoreById(Long storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));
  }

  private Meeting findMeetingById(Long meetingId) {
    return meetingRepository.findById(meetingId)
        .orElseThrow(() -> new CustomException(MEETING_NOT_FOUND));
  }
}
