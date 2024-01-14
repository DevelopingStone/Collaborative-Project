package com.zerobase.oriticket.domain.chat.dto;

import com.zerobase.oriticket.domain.chat.constants.ContactSenderType;
import com.zerobase.oriticket.domain.chat.entity.ContactChatMessage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ContactChatMessageResponse {

    private Long contactChatMessageId;
    private Long contactChatRoomId;
    private ContactSenderType senderType;
    private String message;
    private LocalDateTime chattedAt;

    public static ContactChatMessageResponse fromEntity(ContactChatMessage contactChatMessage){

        return ContactChatMessageResponse.builder()
                .contactChatMessageId(contactChatMessage.getContactChatMessageId())
                .contactChatRoomId(contactChatMessage.getContactChatRoom().getContactChatRoomId())
                .senderType(contactChatMessage.getSenderType())
                .message(contactChatMessage.getMessage())
                .chattedAt(contactChatMessage.getChattedAt())
                .build();
    }
}
