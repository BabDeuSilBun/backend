package com.zerobase.babdeusilbun.swagger.annotation.snapshot;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface PurchaseSnapshotSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "주문 후 공동 주문 스냅샷 리스트 조회")
  @Parameters(value = {
      @Parameter(name = "userDetails"),
      @Parameter(name = "meetingId"),
      @Parameter(name = "pageable")
  })
  @Tag(name = "PurchaseSnapshot")
  @interface GetTeamPurchaseSnapshotsSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "주문 후 개별 주문 스냅샷 리스트 조회")
  @Parameters(value = {
      @Parameter(name = "userDetails"),
      @Parameter(name = "meetingId"),
      @Parameter(name = "pageable")
  })
  @Tag(name = "PurchaseSnapshot")
  @interface GetIndividualPurchaseSnapshotsSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "주문 후 주문 스냅샷 조회")
  @Parameters(value = {
      @Parameter(name = "userDetails"),
      @Parameter(name = "meetingId"),
  })
  @Tag(name = "PurchaseSnapshot")
  @interface GetPurchaseSnapshotsSwagger {}



}
