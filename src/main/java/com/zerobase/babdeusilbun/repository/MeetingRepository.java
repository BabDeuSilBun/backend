package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {


  @Query( "select m from meeting m "
          + "where m.leader = :user "
          + "and m.status != 'MEETING_CANCELLED' and m.status != 'MEETING_COMPLETED' "
      + "union "
        + "select m from purchase p "
          + "join p.meeting m "
          + "where p.user = :user "
          + "and m.status != 'MEETING_CANCELLED' and m.status != 'MEETING_COMPLETED' ")
  List<Meeting> findProceedingByUser(User user);
}
