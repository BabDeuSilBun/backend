package com.zerobase.babdeusilbun.service.impl;

import com.zerobase.babdeusilbun.domain.*;
import com.zerobase.babdeusilbun.dto.TeamPurchaseDto;
import com.zerobase.babdeusilbun.enums.MeetingStatus;
import com.zerobase.babdeusilbun.enums.PurchaseStatus;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.repository.*;
import com.zerobase.babdeusilbun.service.TeamPurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.babdeusilbun.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamPurchaseServiceImpl implements TeamPurchaseService {
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final PurchaseRepository purchaseRepository;
    private final MenuRepository menuRepository;
    private final TeamPurchaseRepository teamPurchaseRepository;

    @Override
    public TeamPurchase createTeamPurchase(Long userId, Long meetingId, TeamPurchaseDto.CreateRequest request) {
        // 사용자 정보 찾기, 없으면 예외처리
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 미팅 정보 찾기, 없으면 예외처리
        Meeting meeting = meetingRepository.findAllByIdAndDeletedAtIsNull(meetingId)
                .orElseThrow(() -> new CustomException(MEETING_NOT_FOUND));

        // 메뉴 정보 찾기, 없으면 예외처리
        Menu menu = menuRepository.findByIdAndDeletedAtIsNull(request.getMenuId())
                .orElseThrow(() -> new CustomException(MENU_NOT_FOUND));

        // 1. 현재 미팅의 리더인지 확인, 아니면 예외 처리
        if(meeting.getLeader().getId() != userId) {
            throw new CustomException(MEETING_LEADER_NOT_MATCH);
        }

        // 2. 현재 입력한 메뉴가 속한 매장 식별번호가 미팅 속 매장의 식별번호와 동일한지 확인, 아니면 예외 처리
        if(menu.getStore().getId() != meeting.getStore().getId()) {
            throw new CustomException(STORE_NOT_INCLUDE_MENU);
        }

        // 3. 모집중인 미팅인지 확인, 아니면 예외 처리
        if(meeting.getStatus() != MeetingStatus.GATHERING) {
            throw new CustomException(MEETING_STATUS_INVALID);
        }

        // 4. 현재 유저가 현재 미팅에 등록했던 팀 주문 중에서 이미 같은 음식을 등록했는지 확인, 아니면 예외처리
        if(teamPurchaseRepository.existsAllByMenuAndMeeting(menu, meeting)) {
            throw new CustomException(ALREADY_EXIST_TEAM_PURCHASE);
        }

        // 팀주문 새로 등록
        TeamPurchase teamPurchase = TeamPurchase.
                builder().
                meeting(meeting).
                menu(menu).
                quantity(request.getQuantity()).
                paymentPrice(menu.getPrice() * request.getQuantity()).
                build();

        teamPurchaseRepository.save(teamPurchase);

        return teamPurchase;
    }

    @Override
    @Transactional
    public TeamPurchase updateTeamPurchase(Long userId, Long purchaseId, TeamPurchaseDto.UpdateRequest request) {
        // 사용자 정보 찾기, 없으면 예외처리
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 팀구매 정보 찾기, 없으면 예외처리
        TeamPurchase teamPurchase = teamPurchaseRepository.findAllById(purchaseId)
                .orElseThrow(() -> new CustomException(PURCHASE_NOT_FOUND));

        // 1. 현재 미팅의 리더인지 확인, 아니면 예외 처리
        if(teamPurchase.getMeeting().getLeader().getId() != user.getId()) {
            throw new CustomException(MEETING_LEADER_NOT_MATCH);
        }

        // 2. 모집중인 미팅인지 확인, 아니면 예외 처리
        if(teamPurchase.getMeeting().getStatus() != MeetingStatus.GATHERING) {
            throw new CustomException(MEETING_STATUS_INVALID);
        }

        // 개인구매 수량 정보 갱신
        teamPurchase.updateQuantity(request.getQuantity());

        return teamPurchase;
    }

    @Override
    public void deleteTeamPurchase(Long userId, Long purchaseId) {
        // 사용자 정보 찾기, 없으면 예외처리
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 팀구매 정보 찾기, 없으면 예외처리
        TeamPurchase teamPurchase = teamPurchaseRepository.findAllById(purchaseId)
                .orElseThrow(() -> new CustomException(PURCHASE_NOT_FOUND));

        // 1. 현재 미팅의 리더인지 확인, 아니면 예외 처리
        if(teamPurchase.getMeeting().getLeader().getId() != user.getId()) {
            throw new CustomException(MEETING_LEADER_NOT_MATCH);
        }

        // 2. 모집중인 미팅인지 확인, 아니면 예외 처리
        if(teamPurchase.getMeeting().getStatus() != MeetingStatus.GATHERING) {
            throw new CustomException(MEETING_STATUS_INVALID);
        }

        teamPurchaseRepository.delete(teamPurchase);
    }
}
