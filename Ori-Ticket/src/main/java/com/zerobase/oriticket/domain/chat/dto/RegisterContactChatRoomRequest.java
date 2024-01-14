package com.zerobase.oriticket.domain.chat.dto;

import com.zerobase.oriticket.domain.chat.entity.ContactChatRoom;
import com.zerobase.oriticket.domain.members.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterContactChatRoomRequest {

    private Long memberId;
    
    public ContactChatRoom toEntity(Member member){
        return ContactChatRoom.builder()
                .member(member)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
