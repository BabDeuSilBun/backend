package com.zerobase.babdeusilbun.controller;

import com.zerobase.babdeusilbun.service.SnapshotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class SnapshotController {

  private final SnapshotService snapshotService;

  // 주문 후 공동 주문 스냅샷 리스트 조회
  // /api/users/meetings/{meetingId}/snapshots/post-order/groups

  // 주문 후 개별 주문 스냅샷 리스트 조회
  // /api/users/{userId}/meetings/{meetingId}/snapshots/post-purchases/individuals

  // 주문 후 주문 스냅샷 조회
  // /api/users/{userId}/meetings/{meetingId}/snapshots/post-purchases/purchases

  // 포인트 스냅샷 리스트 조회
  // /api/users/{userId}/snapshots/points

  // /api/users/{userId}/snapshots/points
  // /api/users/{userId}/meetings/{meetingId}/snapshots/payments

}
