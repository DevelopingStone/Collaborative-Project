package com.zerobase.oriticket.domain.chat.entity;

import com.zerobase.oriticket.domain.members.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage extends BaseChatMessage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;

    @ManyToOne
    private ChatRoom chatRoom;

    @ManyToOne
    private Member member;

}
