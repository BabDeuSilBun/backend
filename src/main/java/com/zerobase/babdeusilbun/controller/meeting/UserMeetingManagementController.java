package com.zerobase.babdeusilbun.controller.meeting;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.zerobase.babdeusilbun.dto.EvaluateDto.EvaluateParticipantRequest;
import com.zerobase.babdeusilbun.dto.MeetingRequest.Create;
import com.zerobase.babdeusilbun.dto.MeetingRequest.Update;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.EvaluateService;
import com.zerobase.babdeusilbun.service.MeetingService;
import com.zerobase.babdeusilbun.swagger.annotation.meeting.UserMeetingManagementSwagger.CreateMeetingSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.meeting.UserMeetingManagementSwagger.EvaluateParticipantSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.meeting.UserMeetingManagementSwagger.UpdateMeetingInfoSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.meeting.UserMeetingManagementSwagger.WithdrawMeetingSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/meetings")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserMeetingManagementController {
  private final MeetingService meetingService;
  private final EvaluateService evaluateService;

  @PostMapping
  @CreateMeetingSwagger
  public ResponseEntity<Void> createMeeting(
      @Validated @RequestBody Create request,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    // TODO
    //  userId
    meetingService.createMeeting(request, userDetails);
    return ResponseEntity.status(CREATED).build();
  }

  @PostMapping("/{meetingId}")
  @UpdateMeetingInfoSwagger
  public ResponseEntity<Void> updateMeetingInfo(
      @PathVariable("meetingId") Long meetingId,
      @Validated @RequestBody Update request,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    // TODO
    //  userId
    meetingService.updateMeeting(meetingId, request, userDetails);

    return ResponseEntity.status(OK).build();
  }

  @DeleteMapping("/{meetingId}")
  @WithdrawMeetingSwagger
  public ResponseEntity<Void> withdrawMeeting(
      @PathVariable("meetingId") Long meetingId,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {

    // TODO
    //  userId
    meetingService.withdrawMeeting(meetingId, userDetails);

    return ResponseEntity.status(OK).build();
  }

  @PostMapping("/{meetingId}/participants/{participantId}")
  @EvaluateParticipantSwagger
  public ResponseEntity<Void> evaluateParticipant(
      @RequestBody EvaluateParticipantRequest request,
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable("meetingId") Long meetingId, @PathVariable("participantId") Long participantId
  ) {

    evaluateService.evaluateParticipant(request, userDetails.getId(), meetingId, participantId);

    return ResponseEntity.status(CREATED).build();
  }
}
