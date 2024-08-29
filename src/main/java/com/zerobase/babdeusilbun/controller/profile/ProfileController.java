package com.zerobase.babdeusilbun.controller.profile;

import com.zerobase.babdeusilbun.dto.UserDto.Profile;
import com.zerobase.babdeusilbun.service.UserService;
import com.zerobase.babdeusilbun.swagger.annotation.meeting.MeetingInformationSwagger.GetMeetingInfoSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileController {
  private final UserService userService;

  /**
   * 프로필 조회
   */
  @GetMapping("/users/{userId}")
  @GetMeetingInfoSwagger
  public ResponseEntity<Profile> getMeetingInfo(@PathVariable Long userId) {
    Profile userProfile = userService.getUserProfile(userId);
    return ResponseEntity.ok(userProfile);
  }
}
