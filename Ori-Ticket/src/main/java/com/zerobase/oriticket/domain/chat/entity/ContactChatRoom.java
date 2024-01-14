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
public class ContactChatRoom extends BaseChatRoom{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactChatRoomId;

    @OneToOne
    private Member member;

}
