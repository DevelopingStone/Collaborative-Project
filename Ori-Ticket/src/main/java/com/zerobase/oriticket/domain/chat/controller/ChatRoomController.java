package com.zerobase.oriticket.domain.chat.controller;

import com.zerobase.oriticket.domain.chat.dto.ChatRoomResponse;
import com.zerobase.oriticket.domain.chat.entity.ChatRoom;
import com.zerobase.oriticket.domain.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping
    public ResponseEntity<ChatRoomResponse> get(
            @RequestParam("id") Long chatRoomId
    ){
        ChatRoom chatRoom = chatRoomService.get(chatRoomId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ChatRoomResponse.fromEntity(chatRoom));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<ChatRoomResponse>> getAll(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        Page<ChatRoom> chatRoom = chatRoomService.getAll(page, size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(chatRoom.map(ChatRoomResponse::fromEntity));
    }

    @GetMapping("/transactions")
    public ResponseEntity<ChatRoomResponse> getByTransaction(
            @RequestParam("id") Long transactionId
    ){
        ChatRoom chatRoom = chatRoomService.getByTransaction(transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ChatRoomResponse.fromEntity(chatRoom));
    }

    @GetMapping("/member")
    public ResponseEntity<List<ChatRoomResponse>> getByMember(
            @RequestParam("id") Long memberId
    ){
        List<ChatRoom> chatRooms = chatRoomService.getByMember(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(chatRooms.stream()
                        .map(ChatRoomResponse::fromEntity)
                        .toList());
    }

}
