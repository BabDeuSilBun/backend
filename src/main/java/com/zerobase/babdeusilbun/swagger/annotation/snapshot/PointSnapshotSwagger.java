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

public @interface PointSnapshotSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "포인트 스냅샷 리스트 조회")
  @Parameter(name = "pageable")
  @Tag(name = "PointSnapshot")
  @interface GetPointSnapshotListSwagger {

  }

}
