package com.zerobase.babdeusilbun.service;

import static com.zerobase.babdeusilbun.dto.SnapshotDto.*;

import com.zerobase.babdeusilbun.dto.SnapshotDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SnapshotService {

  Page<SubPurchaseSnapshot> getTeamPurchaseSnapshots(Long userId, Long meetingId, Pageable pageable);

  Page<SubPurchaseSnapshot> getIndividualPurchaseSnapshots(Long userId, Long meetingId, Pageable pageable);

}
