package com.zerobase.backend.enums;

public enum PointStatus {
    POINT_PLUS("포인트 증가"),
    POINT_MINUS("포인트 감소");

    private final String description;

    PointStatus(String description) {
        this.description = description;
    }
}
