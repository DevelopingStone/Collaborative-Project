package com.zerobase.oriticket.global.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberExceptionStatus {

    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "멤버 정보를 찾을 수 없습니다.");

    private final int code;
    private final String message;
}
