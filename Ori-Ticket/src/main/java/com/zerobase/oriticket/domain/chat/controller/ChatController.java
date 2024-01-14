package com.zerobase.oriticket.domain.chat.controller;

import com.zerobase.oriticket.domain.chat.dto.ChatMessageResponse;
import com.zerobase.oriticket.domain.chat.dto.ContactChatMessageResponse;
import com.zerobase.oriticket.domain.chat.dto.SendChatMessageRequest;
import com.zerobase.oriticket.domain.chat.dto.SendContactChatMessageRequest;
import com.zerobase.oriticket.domain.chat.entity.ChatMessage;
import com.zerobase.oriticket.domain.chat.entity.ContactChatMessage;
import com.zerobase.oriticket.domain.chat.service.ChatMessageService;
import com.zerobase.oriticket.domain.chat.service.ContactChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final ContactChatMessageService contactChatMessageService;

    @MessageMapping("/{chatRoomId}")
    @SendTo("/send/{chatRoomId}")
    public ChatMessageResponse chat(
            @DestinationVariable("chatRoomId") Long chatRoomId,
            SendChatMessageRequest message
    ){
        ChatMessage chatMessage = chatMessageService.register(chatRoomId, message);

        return ChatMessageResponse.fromEntity(chatMessage);
    }

    @MessageMapping("/contact/{contactChatRoomId}")
    @SendTo("/send/contact/{contactChatRoomId}")
    public ContactChatMessageResponse contact(
            @DestinationVariable("contactChatRoomId") Long contactChatRoomId,
            SendContactChatMessageRequest message
    ){
        ContactChatMessage contactChatMessage = contactChatMessageService.register(contactChatRoomId, message);

        return ContactChatMessageResponse.fromEntity(contactChatMessage);
    }
}
