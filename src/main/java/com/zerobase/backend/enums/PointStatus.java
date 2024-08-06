package com.zerobase.backend.enums;

public enum PointStatus {
    PLUS("포인트 증가"),
    MINUS("포인트 감소");

    private final String description;

    PointStatus(String description) {
        this.description = description;
    }
}
