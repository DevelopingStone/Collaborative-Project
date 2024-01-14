package com.zerobase.oriticket.global.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

// InfoStatus.java
@Getter
@AllArgsConstructor
public enum InfoStatus {
    FOUND("Found", "정보 확인 성공 상태"),
    NOT_FOUND("NotFound", "정보 확인 실패 상태");

    private final String status;
    private final String description;
}

