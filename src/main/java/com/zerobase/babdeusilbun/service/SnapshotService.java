package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.domain.IndividualPurchasePayment;
import com.zerobase.babdeusilbun.domain.Point;
import com.zerobase.babdeusilbun.domain.PurchasePayment;
import com.zerobase.babdeusilbun.domain.TeamPurchasePayment;
import com.zerobase.babdeusilbun.dto.SnapshotDto.PointSnapshot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SnapshotService {

  Page<TeamPurchasePayment> getTeamPurchaseSnapshots(Long userId, Long meetingId, Pageable pageable);

  Page<IndividualPurchasePayment> getIndividualPurchaseSnapshots(Long userId, Long meetingId, Pageable pageable);

  PurchasePayment getPurchaseSnapshot(Long userId, Long meetingId);

  Page<Point> getPointSnapshotList(Long userId, Pageable pageable);
}
