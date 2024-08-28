package com.zerobase.babdeusilbun.swagger.annotation.meeting;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface MeetingDetailSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "모임 정보 조회")
  @Parameter(name = "meetingId")
  @Tag(name = "MeetingDetail")
  @interface GetMeetingInfoSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "모임장 조회")
  @Parameter(name = "meetingId")
  @Tag(name = "MeetingDetail")
  @interface GetMeetingLeaderInfoSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "모임원 조회")
  @Parameters(value = {
      @Parameter(name = "meetingId"),
      @Parameter(name = "pageable")
  })
  @Tag(name = "MeetingDetail")
  @interface GetMeetingParticipantInfoSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "모임 현재 참가자 수 조회")
  @Parameter(name = "meetingId")
  @Tag(name = "MeetingDetail")
  @interface GetMeetingHeadCount {

  }

}
