package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.ChatRoom;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.ChatDto.RoomInformation;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
  Optional<ChatRoom> findByMeeting(Meeting meeting);

  @Query("SELECT COUNT(DISTINCT cr.id) FROM ChatRoom cr " +
      "JOIN Chat c ON c.chatRoom = cr " +
      "JOIN cr.meeting m " +
      "JOIN m.store s " +
      "WHERE c.user = :user " +
      "AND NOT EXISTS (" +
        "SELECT 1 FROM Chat c2 " +
        "WHERE c2.chatRoom = cr " +
        "AND c2.user = :user " +
        "AND c2.createdAt = (" +
          "SELECT MAX(c3.createdAt) FROM Chat c3 " +
          "WHERE c3.chatRoom = cr " +
          "AND c3.user = :user" +
        ") " +
        "AND c2.type = 'LEAVE'" +
      ")")
  int countRoomInformationByUser(@Param("user") User user);

  @Query("SELECT cr FROM ChatRoom cr " +
      "JOIN Chat c ON c.chatRoom = cr " +
      "JOIN cr.meeting m " +
      "JOIN m.store s " +
      "WHERE c.user = :user " +
      "AND NOT EXISTS (" +
        "SELECT 1 FROM Chat c2 " +
        "WHERE c2.chatRoom = cr " +
        "AND c2.user = :user " +
        "AND c2.createdAt = (" +
          "SELECT MAX(c3.createdAt) " +
          "FROM Chat c3 " +
          "WHERE c3.chatRoom = cr " +
          "AND c3.user = :user" +
        ") " +
        "AND c2.type = 'LEAVE'" +
      ") " +
      "GROUP BY cr.id " +
      "ORDER BY MAX(c.createdAt) DESC")
  Page<RoomInformation> findRoomInformationByUser(@Param("user") User user, Pageable pageable);
}
