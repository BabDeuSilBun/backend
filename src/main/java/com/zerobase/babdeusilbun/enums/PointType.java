package com.zerobase.babdeusilbun.enums;

import lombok.Getter;

@Getter
public enum PointType {
    PLUS("포인트를 획득하였습니다", "포인트 증가"),
    MINUS("포인트를 사용하였습니다", "포인트 감소");

    private final String content;
    private final String description;

    PointType(String content, String description) {
        this.content = content;
        this.description = description;
    }
}
