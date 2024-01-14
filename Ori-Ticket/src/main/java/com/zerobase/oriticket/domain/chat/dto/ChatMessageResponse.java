package com.zerobase.oriticket.domain.chat.dto;

import com.zerobase.oriticket.domain.chat.entity.ChatMessage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessageResponse {

    private Long chatMessageId;
    private Long chatRoomId;
    private Long memberId;
    private String message;
    private LocalDateTime chattedAt;

    public static ChatMessageResponse fromEntity(ChatMessage chatMessage){

        return ChatMessageResponse.builder()
                .chatMessageId(chatMessage.getChatMessageId())
                .chatRoomId(chatMessage.getChatRoom().getChatRoomId())
                .memberId(chatMessage.getMember().getMemberId())
                .message(chatMessage.getMessage())
                .chattedAt(chatMessage.getChattedAt())
                .build();
    }
}
