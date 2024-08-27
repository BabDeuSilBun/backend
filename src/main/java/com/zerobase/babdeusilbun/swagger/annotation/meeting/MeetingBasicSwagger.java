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

public @interface MeetingBasicSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "모임리스트 목록 조회/검색",
      description = "정렬: 결제 마감 시간 순, 배송시간 짧은 순, 배달비 적은 순, 최소주문금액 낮은 순"
  )
  @Parameters(value = {
      @Parameter(name = "schoolId"),
      @Parameter(name = "sortCriteria"),
      @Parameter(name = "searchMenu"),
      @Parameter(name = "categoryFilter"),
      @Parameter(name = "pageable")
  })
  @Tag(name = "MeetingBasic")
  @interface GetAllMeetingListSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "모임 생성")
  @Parameters(value = {
      @Parameter(name = "request"),
      @Parameter(name = "userDetails")
  })
  @Tag(name = "MeetingBasic")
  @interface CreateMeetingSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "가게 주문 전 모임 정보 수정")
  @Parameters(value = {
      @Parameter(name = "meetingId"),
      @Parameter(name = "request"),
      @Parameter(name = "userDetails")
  })
  @Tag(name = "MeetingBasic")
  @interface UpdateMeetingInfoSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "모임 탈퇴/취소")
  @Parameters(value = {
      @Parameter(name = "meetingId"),
      @Parameter(name = "userDetails")
  })
  @Tag(name = "MeetingBasic")
  @interface WithdrawMeetingSwagger {}


}
