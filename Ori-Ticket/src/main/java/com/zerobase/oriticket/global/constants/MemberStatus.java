package com.zerobase.oriticket.global.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberStatus {

    ACTIVE("ACTIVE", "정상"),
    SUSPENDED("SUSPENDED", "활동정지"),
    PERMANENTLY_SUSPENDED("PERMANENTLY_SUSPENDED", "영구정지");

    private final String status;
    private final String description;
}