package com.zerobase.babdeusilbun.swagger.enums;

import io.swagger.v3.oas.models.tags.Tag;
import lombok.Getter;

@Getter
public enum SwaggerTag {
  USER_AUTH("User Auth Api", "일반 이용자 권한 Api"),
  USER_CHAT("User Chat Api", "일반 이용자의 채팅 Api"),
  USER_TEAM_CART("User Team Cart Api", "일반 이용자의 공동 장바구니 Api"),
  USER_INDIVIDUAL_CART("User Individual Cart Api", "일반 이용자의 개인 장바구니 Api"),
  USER_INQUIRY("User Inquiry Api", "일반 이용자의 신고/건의 Api"),
  USER_LOOKUP("User Lookup Api", "일반 이용자의 기타 검색, 조회 Api"),
  USER_MEETING_INFORMATION("User Meeting Information Api", "일반 이용자의 모임 정보 조회 Api"),
  USER_MEETING_MANAGEMENT("User Meeting Management Api", "일반 이용자의 모임 관리 Api"),
  USER_PAYMENT("User Payment Api", "일반 이용자의 결제 Api"),
  USER_POINT("User Point Api", "일반 이용자의 포인트 Api"),
  USER_PROFILE("User Profile Api", "일반 이용자의 회원정보 Api"),
  USER_PURCHASE("User Purchase Api", "일반 이용자의 주문 Api"),
  USER_SIGN("User Sign Api", "일반 이용자의 회원가입, 인증 Api"),
  USER_STORE_INFORMATION("User Store Information Api", "일반 이용자의 상점 정보 조회 Api"),

  ENTREPRENEUR_AUTH("Entrepreneur Auth Api", "사업가 권한 Api"),
  ENTREPRENEUR_LOOKUP("Entrepreneur Lookup Api", "사업가의 기타 검색, 조회 Api"),
  ENTREPRENEUR_MEETING_INFORMATION("Entrepreneur Meeting Information Api", "사업가의 모임 정보 조회 Api"),
  ENTREPRENEUR_MENU("Entrepreneur Menu Api", "사업가의 메뉴 관리 Api"),
  ENTREPRENEUR_PROFILE("Entrepreneur Profile Api", "사업가의 회원정보 Api"),
  ENTREPRENEUR_PURCHASE("Entrepreneur Purchase Api", "사업가의 주문 Api"),
  ENTREPRENEUR_SIGN("Entrepreneur Sign Api", "사업가의 회원가입, 인증 Api"),
  ENTREPRENEUR_STORE_INFORMATION("Entrepreneur Store Information Api", "사업가의 상점 정보 조회 Api"),
  ENTREPRENEUR_STORE_MANAGEMENT("Entrepreneur Store Management Api", "사업가의 상점 관리 Api"),

  COMMON_LOOkUP("Common Lookup Api", "기타 검색, 조회 Api"),
  COMMON_SIGN("Common Sign Api", "회원가입, 인증 Api"),
  COMMON_STORE_INFORMATION("Common Store Information Api", "상점 정보 조회 Api")



  ;

  private final String name;
  private final String description;
  private final Tag tag;

  SwaggerTag(String name, String description) {
    this.name = name;
    this.description = description;
    this.tag = new Tag().name(name).description(description);
  }
}
