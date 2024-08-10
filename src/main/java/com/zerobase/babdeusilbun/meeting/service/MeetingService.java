package com.zerobase.babdeusilbun.meeting.service;

import com.zerobase.babdeusilbun.dto.MeetingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeetingService {

  Page<MeetingDto> getAllMeetingList(Long schoolId, String sortCriteria, String searchMenu, Pageable pageable);
}
