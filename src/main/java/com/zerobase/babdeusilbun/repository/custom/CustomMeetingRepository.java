package com.zerobase.babdeusilbun.repository.custom;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomMeetingRepository {

  Page<Meeting> findFilteredMeetingList
      (Long schoolId, String sortParameter, String searchMenu, Long categoryFilter, Pageable pageable);

//  Page<User> findAllParticipantFromMeeting(Long meetingId, Pageable pageable);

//  Long getParticipantCount(Long meetingId);
}
