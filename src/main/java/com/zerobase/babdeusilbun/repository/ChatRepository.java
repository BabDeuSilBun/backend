package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Chat;
import com.zerobase.babdeusilbun.domain.ChatRoom;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.enums.ChatType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<Chat, Long> {
  boolean existsByTypeAndUserAndChatRoom(ChatType type, User user, ChatRoom chatRoom);

  @Query("SELECT COUNT(c) " +
      "FROM Chat c " +
      "WHERE c.chatRoom = :chatRoom " +
      "AND c.createdAt > (" +
      "  SELECT MIN(c2.createdAt) " +
      "  FROM Chat c2 " +
      "  WHERE c2.chatRoom = :chatRoom " +
      "  AND c2.user = :user " +
      "  AND c2.type = 'ENTER'" +
      ")")
  int countChatsInRoomAfterUserEntered(@Param("chatRoom") ChatRoom chatRoom, @Param("user") User user);

  @Query("SELECT c " +
      "FROM Chat c " +
      "WHERE c.chatRoom = :chatRoom " +
      "AND c.createdAt > (" +
      "  SELECT MIN(c2.createdAt) " +
      "  FROM Chat c2 " +
      "  WHERE c2.chatRoom = :chatRoom " +
      "  AND c2.user = :user " +
      "  AND c2.type = 'ENTER'" +
      ") " +
      "ORDER BY c.createdAt DESC")
  Page<Chat> findChatsInRoomAfterUserEntered(
      @Param("chatRoom") ChatRoom chatRoom, @Param("user") User user, Pageable pageable);
}
