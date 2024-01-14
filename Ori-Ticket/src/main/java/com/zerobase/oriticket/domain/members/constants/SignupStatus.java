package com.zerobase.oriticket.domain.members.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

// SignupStatus.java
@Getter
@AllArgsConstructor
public enum SignupStatus {
    PENDING("Pending", "가입 대기 상태"),
    APPROVED("Approved", "가입 승인 상태"),
    REJECTED("Rejected", "가입 거부 상태");

    private final String status;
    private final String description;
}

