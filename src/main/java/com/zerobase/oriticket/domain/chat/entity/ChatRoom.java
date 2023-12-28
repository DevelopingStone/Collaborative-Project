package com.zerobase.oriticket.domain.chat.entity;

import com.zerobase.oriticket.domain.post.entity.Post;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom extends BaseChatRoom{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @OneToOne
    private Transaction transaction;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> members;

    private LocalDateTime endedAt;

    public static ChatRoom createChatRoom(Transaction transaction, Post salePost){
        Set<Long> members = new HashSet<>();
        members.add(transaction.getMemberId());
        members.add(salePost.getMember().getMembersId());

        return ChatRoom.builder()
                .transaction(transaction)
                .members(members)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
