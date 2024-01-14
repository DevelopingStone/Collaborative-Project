package com.zerobase.oriticket.global.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostExceptionStatus {

    SALE_POST_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "판매 글 정보를 찾을 수 없습니다."),
    SPORTS_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "스포츠 정보를 찾을 수 없습니다."),
    STADIUM_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "경기장 정보를 찾을 수 없습니다."),
    AWAY_TEAM_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "팀 정보를 찾을 수 없습니다."),
    LIKES_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "찜하기 정보를 찾을 수 없습니다."),
    CANNOT_REGISTER_LIKES_EXIST_LIKES(HttpStatus.BAD_REQUEST.value(), "이미 등록된 찜하기 정보입니다. 찜하기 등록을 할 수 없습니다."),
    CANNOT_DELETE_POST_EXIST_TRANSACTION(HttpStatus.BAD_REQUEST.value(), "생성된 거래가 있습니다. 판매글을 삭제 할 수 없습니다."),
    CANNOT_DELETE_SPORTS_EXIST_TICKET(HttpStatus.BAD_REQUEST.value(), "생성된 티켓이 있습니다. 스포츠를 삭제할 수 없습니다."),
    CANNOT_DELETE_STADIUM_EXIST_TICKET(HttpStatus.BAD_REQUEST.value(), "생성된 티켓이 있습니다. 경기장을 삭제할 수 없습니다."),
    CANNOT_DELETE_AWAY_TEAM_EXIST_TICKET(HttpStatus.BAD_REQUEST.value(), "생성된 티켓이 있습니다. 팀 정보를 삭제할 수 없습니다."),
    CANNOT_MODIFY_POST_STATE_OF_SOLD(HttpStatus.BAD_REQUEST.value(), "판매 완료된 글은 상태를 변경할 수 없습니다."),
    CANNOT_MODIFY_POST_STATE_OF_REPORTED(HttpStatus.BAD_REQUEST.value(), "신고된 글은 상태를 변경할 수 없습니다.");

    private final int code;
    private final String message;
}
