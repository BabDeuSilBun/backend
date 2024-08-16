package com.zerobase.babdeusilbun.meeting.service;

import com.zerobase.babdeusilbun.dto.MeetingDto;
import com.zerobase.babdeusilbun.meeting.dto.MeetingHeadCountDto;
import com.zerobase.babdeusilbun.meeting.dto.MeetingUserDto;
import com.zerobase.babdeusilbun.meeting.dto.MeetingRequest;
import com.zerobase.babdeusilbun.meeting.dto.MeetingRequest.Update;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeetingService {

  Page<MeetingDto> getAllMeetingList(Long schoolId, String sortCriteria, String searchMenu,
      Long categoryFilter, Pageable pageable);

  MeetingDto getMeetingInfo(Long meetingId);

  void createMeeting(MeetingRequest.Create request, CustomUserDetails userDetails);

  void updateMeeting(Long meetingId, Update request, CustomUserDetails userDetails);

  void withdrawMeeting(Long meetingId, CustomUserDetails userDetails);

  MeetingUserDto getMeetingLeaderInfo(Long meetingId);

  Page<MeetingUserDto> getMeetingParticipants(Long meetingId, Pageable pageable);

  MeetingHeadCountDto getMeetingHeadCount(Long meetingId);
}
