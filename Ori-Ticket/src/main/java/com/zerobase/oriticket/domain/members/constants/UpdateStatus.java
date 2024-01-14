package com.zerobase.oriticket.domain.members.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

// UpdateStatus.java
@Getter
@AllArgsConstructor
public enum UpdateStatus {
    SUCCESS("Success", "정보 수정 성공 상태"),
    FAILURE("Failure", "정보 수정 실패 상태");

    private final String status;
    private final String description;
}

