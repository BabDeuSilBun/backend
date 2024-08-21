package com.zerobase.babdeusilbun.service;

import static com.zerobase.babdeusilbun.dto.SnapshotDto.*;

import com.zerobase.babdeusilbun.domain.IndividualPurchasePayment;
import com.zerobase.babdeusilbun.domain.TeamPurchasePayment;
import com.zerobase.babdeusilbun.dto.SnapshotDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SnapshotService {

  Page<TeamPurchasePayment> getTeamPurchaseSnapshots(Long userId, Long meetingId, Pageable pageable);

  Page<IndividualPurchasePayment> getIndividualPurchaseSnapshots(Long userId, Long meetingId, Pageable pageable);

}
