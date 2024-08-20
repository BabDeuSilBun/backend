package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.MeetingDto;
import com.zerobase.babdeusilbun.dto.MeetingRequest;
import com.zerobase.babdeusilbun.dto.MeetingRequest.Update;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeetingService {

  Page<MeetingDto> getAllMeetingDtoList(Long schoolId, String sortCriteria, String searchMenu, Long categoryFilter, Pageable pageable);

  Page<Meeting> getAllMeetingList
      (Long schoolId, String sortCriteria, String searchMenu, Long categoryFilter, Pageable pageable);

  MeetingDto getMeetingInfoDto(Long meetingId);

  Meeting getMeetingInfo(Long meetingId);

  void createMeeting(MeetingRequest.Create request, CustomUserDetails userDetails);

  void updateMeeting(Long meetingId, Update request, CustomUserDetails userDetails);

  void withdrawMeeting(Long meetingId, CustomUserDetails userDetails);

  User getMeetingLeaderInfo(Long meetingId);

  Page<User> getMeetingParticipants(Long meetingId, Pageable pageable);

  int getMeetingHeadCount(Long meetingId);

  void confirmParticipant(Long userId, Long meetingId, Long purchaseId);
}
