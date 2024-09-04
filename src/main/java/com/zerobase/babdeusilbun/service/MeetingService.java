package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.ChatDto;
import com.zerobase.babdeusilbun.dto.MeetingDto;
import com.zerobase.babdeusilbun.dto.MeetingRequest;
import com.zerobase.babdeusilbun.dto.MeetingRequest.Update;
import com.zerobase.babdeusilbun.dto.PurchaseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeetingService {

  Page<MeetingDto> getAllMeetingDtoList(Long userId, Long schoolId,
      String sortCriteria, String searchMenu, Long categoryFilter, Pageable pageable);

  Page<Meeting> getAllMeetingList
      (Long schoolId, String sortCriteria, String searchMenu, Long categoryFilter, Pageable pageable);

  MeetingDto getMeetingInfoDto(Long meetingId);

  Meeting getMeetingInfo(Long meetingId);

  void createMeeting(Long userId, MeetingRequest.Create request);

  void updateMeeting(Long userId, Long meetingId, Update request);

  void withdrawMeeting(Long userId, Long meetingId);

  void sendPurchaseToStore(Long userId, Long meetingId);

  User getMeetingLeaderInfo(Long meetingId);

  Page<User> getMeetingParticipants(Long meetingId, Pageable pageable);

  int getMeetingHeadCount(Long meetingId);

  void confirmMeetingPurchase(Long entrepreneurId, Long meetingId);

  void denyMeetingPurchase(Long entrepreneurId, Long meetingId);

  void completeMeetingPurchase(Long entrepreneurId, Long meetingId);

  void sendMessageForDelayMeetingPurchases(Long entrepreneurId, Long meetingId, ChatDto.Request request);
  Page<PurchaseDto.MenuResponse> getMeetingPurchaseByStoreIdAndMeetingId(
      Long entrepreneurId, Long storeId, Long meetingId, int page, int size);

}
