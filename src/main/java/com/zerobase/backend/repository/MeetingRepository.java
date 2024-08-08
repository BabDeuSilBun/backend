package com.zerobase.backend.repository;

import com.zerobase.backend.domain.Meeting;
import com.zerobase.backend.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

  List<Meeting> findAllByLeader(User leader);

  @Query("select m "
        + "from purchase p "
        + "join p.meeting m "
        + "where p.user = :participant")
  List<Meeting> findAllByParticipant(User participant);
}
