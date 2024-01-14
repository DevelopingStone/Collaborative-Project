package com.zerobase.oriticket.global.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ChatExceptionStatus {

    CHAT_ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "채팅 방을 찾을 수 없습니다."),
    ALREADY_ENDED_CHAT_ROOM(HttpStatus.BAD_REQUEST.value(), "이미 종료된 채팅방입니다.");

    private final int code;
    private final String message;
}
