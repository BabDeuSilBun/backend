package com.zerobase.babdeusilbun.enums;

public enum PointType {
    PLUS("포인트 증가"),
    MINUS("포인트 감소");

    private final String description;

    PointType(String description) {
        this.description = description;
    }
}
