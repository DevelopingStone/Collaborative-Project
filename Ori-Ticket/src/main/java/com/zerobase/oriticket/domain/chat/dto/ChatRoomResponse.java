package com.zerobase.oriticket.domain.chat.dto;

import com.zerobase.oriticket.domain.chat.entity.ChatRoom;
import com.zerobase.oriticket.domain.members.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Builder
public class ChatRoomResponse {

    private Long chatRoomId;
    private Long salePostId;
    private Long transactionId;
    private List<ChatRoomMembersResponse> members;
    private LocalDateTime createdAt;
    private LocalDateTime endedAt;

    public static ChatRoomResponse fromEntity(ChatRoom chatRoom){

        List<Member> membersList = new ArrayList<>(chatRoom.getMembers());
        List<ChatRoomMembersResponse> membersResponseList = membersList.stream()
                .map(ChatRoomMembersResponse::fromEntity)
                .sorted(Comparator.comparing(ChatRoomMembersResponse::getMemberId))
                .collect(Collectors.toList());

        return ChatRoomResponse.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .salePostId(chatRoom.getTransaction().getSalePost().getSalePostId())
                .transactionId(chatRoom.getTransaction().getTransactionId())
                .members(membersResponseList)
                .createdAt(chatRoom.getCreatedAt())
                .endedAt(chatRoom.getEndedAt())
                .build();
    }
}
