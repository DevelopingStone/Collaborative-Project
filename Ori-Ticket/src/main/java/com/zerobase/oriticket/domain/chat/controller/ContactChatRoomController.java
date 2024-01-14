package com.zerobase.oriticket.domain.chat.controller;

import com.zerobase.oriticket.domain.chat.dto.ContactChatRoomResponse;
import com.zerobase.oriticket.domain.chat.dto.RegisterContactChatRoomRequest;
import com.zerobase.oriticket.domain.chat.entity.ContactChatRoom;
import com.zerobase.oriticket.domain.chat.service.ContactChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactChatRoomController {

    private final ContactChatRoomService contactChatRoomService;

    @PostMapping
    public ResponseEntity<ContactChatRoomResponse> register(
            @RequestBody RegisterContactChatRoomRequest request
    ){
        ContactChatRoom contactChatRoom =
                contactChatRoomService.register(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ContactChatRoomResponse.fromEntity(contactChatRoom));
    }

    @GetMapping
    public ResponseEntity<ContactChatRoomResponse> get(
            @RequestParam("id") Long contactChatRoomId
    ){
        ContactChatRoom contactChatRoom =
                contactChatRoomService.get(contactChatRoomId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ContactChatRoomResponse.fromEntity(contactChatRoom));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<ContactChatRoomResponse>> getAll(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        Page<ContactChatRoom> contactChatRoom =
                contactChatRoomService.getAll(page, size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(contactChatRoom.map(ContactChatRoomResponse::fromEntity));
    }

    @GetMapping("/member")
    public ResponseEntity<ContactChatRoomResponse> getByMember(
            @RequestParam("id") Long memberId
    ){
        ContactChatRoom contactChatRoom =
                contactChatRoomService.getByMember(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ContactChatRoomResponse.fromEntity(contactChatRoom));
    }
}
