package com.zerobase.babdeusilbun.service.impl;

import com.zerobase.babdeusilbun.repository.IndividualPurchaseRepository;
import com.zerobase.babdeusilbun.repository.PurchasePaymentRepository;
import com.zerobase.babdeusilbun.repository.TeamPurchasePaymentRepository;
import com.zerobase.babdeusilbun.service.SnapshotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SnapshotServiceImpl implements SnapshotService {

  private final IndividualPurchaseRepository individualPurchaseRepository;
  private final TeamPurchasePaymentRepository teamPurchasePaymentRepository;
  private final PurchasePaymentRepository purchasePaymentRepository;

}
