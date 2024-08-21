package com.zerobase.babdeusilbun.service.impl;

import com.zerobase.babdeusilbun.domain.*;
import com.zerobase.babdeusilbun.dto.IndividualPurchaseDto;
import com.zerobase.babdeusilbun.enums.MeetingStatus;
import com.zerobase.babdeusilbun.enums.PurchaseStatus;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.repository.*;
import com.zerobase.babdeusilbun.service.IndividualPurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.babdeusilbun.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class IndividualPurchaseServiceImpl implements IndividualPurchaseService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final PurchaseRepository purchaseRepository;
    private final MenuRepository menuRepository;
    private final IndividualPurchaseRepository individualPurchaseRepository;

    @Override
    @Transactional
    public IndividualPurchase createIndividualPurchase(Long userId, Long meetingId, IndividualPurchaseDto.CreateRequest request) {
        // 사용자 정보 찾기, 없으면 예외처리
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 미팅 정보 찾기, 없으면 예외처리
        Meeting meeting = meetingRepository.findAllByIdAndDeletedAtIsNull(meetingId)
                .orElseThrow(() -> new CustomException(MEETING_NOT_FOUND));

        // 메뉴 정보 찾기, 없으면 예외처리
        Menu menu = menuRepository.findByIdAndDeletedAtIsNull(request.getMenuId())
                .orElseThrow(() -> new CustomException(MENU_NOT_FOUND));

        // 1. 모집중인 미팅인지 확인, 아니면 예외 처리
        if(meeting.getStatus() != MeetingStatus.GATHERING) {
            throw new CustomException(MEETING_STATUS_INVALID);
        }

        // 2. 사용자 정보, 미팅정보를 기반으로 Purchase가 이미 있는지 찾기 (취소상태인건 가져오지 않는다.)
        Purchase purchase = purchaseRepository.findByMeetingAndUserAndStatusIsNot(meeting, user, PurchaseStatus.CANCEL)
                .orElse(null);

        if(purchase == null) { // 3. Purchase가 없는 경우
            // 3-1. 기존에 존재하던 현재 사용자의 주문 전 상태의 Purchase를 모두 취소 처리
            purchaseRepository.updateUserPreviousMeetingPurchaseStatusFromprepurchaseToCancel(meeting, user);
            // 3-2. Purchase 새로 생성
            purchase = Purchase.builder().meeting(meeting).user(user).status(PurchaseStatus.PRE_PURCHASE).build();
            purchaseRepository.save(purchase);
        } else { // 3. Purchase가 있는 경우
            // 3-1.Purchase의 주문 상태가 주문 전인지 확인, 아니면 예외 처리
            if(purchase.getStatus() != PurchaseStatus.PRE_PURCHASE) {
                throw new CustomException(PURCHASE_STATUS_CANCEL);
            }
            // 3-2. 현재 주문 정보를 제외한 존재하던 현재 사용자의 주문 전 상태의 Purchase를 모두 취소 처리
            purchaseRepository.updateUserPreviousMeetingPurchaseStatusFromprepurchaseToCancel(meeting, user);
        }

        // 4. 현재 입력한 메뉴가 속한 매장 식별번호가 미팅 속 매장의 식별번호와 동일한지 확인, 아니면 예외 처리
        if(menu.getStore().getId() != meeting.getStore().getId()) {
            throw new CustomException(STORE_NOT_INCLUDE_MENU);
        }

        // 5. 현재 유저가 현재 미팅에 등록했던 개별 주문 중에서 이미 같은 음식을 등록했는지 확인, 아니면 예외처리
        if(individualPurchaseRepository.existsAllByMenuAndPurchase(menu, purchase)) {
            throw new CustomException(ALREADY_EXIST_INDIVIDUAL_PURCHASE);
        }

        // 개별주문 새로 등록
        IndividualPurchase individualPurchase = IndividualPurchase.
                builder().
                menu(menu).
                purchase(purchase).
                quantity(request.getQuantity()).
                build();

        individualPurchaseRepository.save(individualPurchase);

        return individualPurchase;
    }
}
