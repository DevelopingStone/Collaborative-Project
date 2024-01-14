package com.zerobase.oriticket.domain.chat.entity;

import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.post.entity.Post;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Builder.Default
    @ManyToMany(targetEntity = Member.class)
    private List<Member> members = new ArrayList<>();

    private LocalDateTime endedAt;

    public static ChatRoom createChatRoom(Transaction transaction, Post salePost){
        List<Member> members = new ArrayList<>();
        members.add(transaction.getMember());
        members.add(salePost.getMember());

        return ChatRoom.builder()
                .transaction(transaction)
                .members(members)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
