package com.zerobase.oriticket.user.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

// WithdrawalStatus.java
@Getter
@AllArgsConstructor
public enum WithdrawalStatus {
    SUCCESS("Success", "회원 탈퇴 성공 상태"),
    FAILURE("Failure", "회원 탈퇴 실패 상태");

    private final String status;
    private final String description;
}

