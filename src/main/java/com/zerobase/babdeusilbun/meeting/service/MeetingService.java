package com.zerobase.babdeusilbun.meeting.service;

import com.zerobase.babdeusilbun.dto.MeetingDto;
import com.zerobase.babdeusilbun.meeting.dto.MeetingRequest;
import com.zerobase.babdeusilbun.meeting.dto.MeetingRequest.Update;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface MeetingService {

  Page<MeetingDto> getAllMeetingList(Long schoolId, String sortCriteria, String searchMenu,
      Long categoryFilter, Pageable pageable);

  MeetingDto getMeetingInfo(Long meetingId);

  void createMeeting(MeetingRequest.Create request, UserDetails userDetails);

  void updateMeeting(Long meetingId, Update request, UserDetails userDetails);
}
