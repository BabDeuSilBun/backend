package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.UserDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

  Optional<User> findByIdAndDeletedAtIsNull(Long userId);
  
  @Query(value=
          "select user.email as email, user.name as name, user.nickname as nickname, user.phoneNumber as phoneNumber, " +
                  "user.bankAccount as bankAccount, user.point as point, user.address as address, user.image as image, user.isBanned as isBanned," +
                  " ifnull(count(purchase.id), 0) as meetingCount, school.name as school, school.campus as campus, major.name as major \n" +
                  "from com.zerobase.babdeusilbun.domain.User as user \n" +
                  "left join com.zerobase.babdeusilbun.domain.Purchase as purchase on user.id = purchase.user.id, \n" +
                  "com.zerobase.babdeusilbun.domain.School as school, \n" +
                  "com.zerobase.babdeusilbun.domain.Major as major \n" +
                  "where user.id = :userId and school.id = user.school.id and major.id = user.major.id \n" +
                  "group by user.id \n")
  Optional<UserDto.MyPage> findMyPageByUserId(Long userId);

  @Query("select count(u.id) "
        + "from users u "
        + "join Purchase p on p.user = u "
        + "where p.meeting.id = :meetingId")
  Long countMeetingParticipant(Long meetingId);

  @Query("select u "
      + "from users u "
      + "join Purchase p on p.user = u "
      + "where p.meeting.id = :meetingId")
  Page<User> findAllMeetingParticipant(Long meetingId, Pageable pageable);

}
