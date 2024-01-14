package com.zerobase.oriticket.domain.chat.entity;

import com.zerobase.oriticket.domain.chat.constants.ContactSenderType;
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
public class ContactChatMessage extends BaseChatMessage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactChatMessageId;

    @ManyToOne
    private ContactChatRoom contactChatRoom;

    private ContactSenderType senderType;
}
