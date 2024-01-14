package com.zerobase.oriticket.domain.chat.controller;

import com.zerobase.oriticket.domain.chat.dto.ChatMessageResponse;
import com.zerobase.oriticket.domain.chat.dto.ContactChatMessageResponse;
import com.zerobase.oriticket.domain.chat.entity.ChatMessage;
import com.zerobase.oriticket.domain.chat.entity.ContactChatMessage;
import com.zerobase.oriticket.domain.chat.service.ChatMessageService;
import com.zerobase.oriticket.domain.chat.service.ContactChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final ContactChatMessageService contactChatMessageService;

    @GetMapping
    public ResponseEntity<List<ChatMessageResponse>> getAllChatMessage(
            @RequestParam("id") Long chatRoomId
    ){
        List<ChatMessage> chatMessages = chatMessageService.getByRoom(chatRoomId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(chatMessages.stream()
                        .map(ChatMessageResponse::fromEntity)
                        .toList());
    }

    @GetMapping("/contact")
    public ResponseEntity<List<ContactChatMessageResponse>> getAllContactMessage(
            @RequestParam("id") Long contactChatRoomId
    ){
        List<ContactChatMessage> contactChatMessages = contactChatMessageService.getByRoom(contactChatRoomId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(contactChatMessages.stream()
                        .map(ContactChatMessageResponse::fromEntity)
                        .toList());
    }
}
