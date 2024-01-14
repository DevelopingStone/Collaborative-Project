package com.zerobase.oriticket.domain.chat.dto;

import com.zerobase.oriticket.domain.chat.entity.ContactChatRoom;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Builder
public class ContactChatRoomResponse {

    private Long contactChatRoomId;
    private Long memberId;
    private LocalDateTime createdAt;

    public static ContactChatRoomResponse fromEntity(ContactChatRoom contactChatRoom){
        return ContactChatRoomResponse.builder()
                .contactChatRoomId(contactChatRoom.getContactChatRoomId())
                .memberId(contactChatRoom.getMember().getMemberId())
                .createdAt(contactChatRoom.getCreatedAt())
                .build();
    }
}
