package com.zerobase.oriticket.domain.members.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;


// LoginStatus.java
@Getter
@AllArgsConstructor
public enum LoginStatus {
    SUCCESS("Success", "로그인 성공 상태"),
    FAILURE("Failure", "로그인 실패 상태");

    private final String status;
    private final String description;
}

