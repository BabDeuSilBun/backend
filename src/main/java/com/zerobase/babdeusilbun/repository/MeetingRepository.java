package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.repository.custom.CustomMeetingRepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MeetingRepository extends JpaRepository<Meeting, Long>, CustomMeetingRepository {

  @Query("select m from Meeting m "
      + "where m.leader = :leader "
      + "and m.status != 'MEETING_CANCELLED' and m.status != 'MEETING_COMPLETED' ")
  List<Meeting> findProceedingByLeader(User leader);

  @Query("select m from Meeting m "
      + "join Purchase p on p.meeting = m "
      + "where p.user = :participant "
      + "and m.status != 'MEETING_CANCELLED' and m.status != 'MEETING_COMPLETED' ")
  List<Meeting> findProceedingByParticipant(User participant);

}
