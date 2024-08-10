package com.zerobase.babdeusilbun.meeting.service.impl;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.StoreImage;
import com.zerobase.babdeusilbun.dto.DeliveryAddressDto;
import com.zerobase.babdeusilbun.dto.MetAddressDto;
import com.zerobase.babdeusilbun.dto.StoreImageDto;
import com.zerobase.babdeusilbun.dto.MeetingDto;
import com.zerobase.babdeusilbun.meeting.service.MeetingService;
import com.zerobase.babdeusilbun.repository.MeetingQueryRepository;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.StoreImageRepository;
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
  private final MeetingQueryRepository meetingQueryRepository;
  private final StoreImageRepository storeImageRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<MeetingDto> getAllMeetingList(Long schoolId, String sortCriteria, String searchMenu,
      Pageable pageable) {

    return meetingQueryRepository
        .findFilteredMeetingList(schoolId, sortCriteria, searchMenu, pageable)
        .map(this::mapToMeetingDto);
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
}
