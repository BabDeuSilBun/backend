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

public @interface PaymentSnapshotSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "결제 스냅샷 조회")
  @Parameter(name = "meetingId")
  @Tag(name = "PaymentSnapshot")
  @interface GetPaymentSnapshotsSwagger {

  }

}
