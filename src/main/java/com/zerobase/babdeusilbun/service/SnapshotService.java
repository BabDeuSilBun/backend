package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.PurchaseSnapshotDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SnapshotService {

  Page<PurchaseSnapshotDto> getTeamPurchaseSnapshots(Long userId, Long meetingId, Pageable pageable);

  Page<PurchaseSnapshotDto> getIndividualPurchaseSnapshots(Long userId, Long meetingId, Pageable pageable);

}
