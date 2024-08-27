package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.MeetingPurchaseTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingPurchaseTimeRepository extends JpaRepository<MeetingPurchaseTime, Long> {
  Optional<MeetingPurchaseTime> findByMeeting(Meeting meeting);
}
