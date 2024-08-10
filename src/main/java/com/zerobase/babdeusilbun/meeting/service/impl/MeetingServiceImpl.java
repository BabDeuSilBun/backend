package com.zerobase.babdeusilbun.meeting.service.impl;

import static com.zerobase.babdeusilbun.enums.MeetingStatus.*;
import static com.zerobase.babdeusilbun.exception.ErrorCode.*;

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
import com.zerobase.babdeusilbun.meeting.dto.MeetingRequest;
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
  public void createMeeting(MeetingRequest request, UserDetails userDetails) {

    String emailByUserDetails = userDetails.getUsername();
    User findUser = findUserByEmail(emailByUserDetails);

    Meeting meetingFromRequest = createMeetingFromRequest(request, findUser);
    meetingRepository.save(meetingFromRequest);
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

  private Meeting createMeetingFromRequest(MeetingRequest request, User leader) {

    return Meeting.builder()
        .leader(leader)
        .store(findStoreById(request))
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

  private Store findStoreById(MeetingRequest request) {
    return storeRepository.findById(request.getStoreId())
        .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));
  }

  private User findUserByEmail(String emailByUserDetails) {
    return userRepository.findByEmail(emailByUserDetails)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
  }

  private Meeting findMeetingById(Long meetingId) {
    return meetingRepository.findById(meetingId)
        .orElseThrow(() -> new CustomException(MEETING_NOT_FOUND));
  }
}
