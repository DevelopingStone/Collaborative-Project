package com.zerobase.oriticket.domain.chat.service;

import com.zerobase.oriticket.domain.chat.dto.SendContactChatMessageRequest;
import com.zerobase.oriticket.domain.chat.entity.ContactChatMessage;
import com.zerobase.oriticket.domain.chat.entity.ContactChatRoom;
import com.zerobase.oriticket.domain.chat.repository.ContactChatMessageRepository;
import com.zerobase.oriticket.domain.chat.repository.ContactChatRoomRepository;
import com.zerobase.oriticket.global.exception.impl.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zerobase.oriticket.global.constants.ChatExceptionStatus.CHAT_ROOM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ContactChatMessageService {

    private final ContactChatRoomRepository contactChatRoomRepository;
    private final ContactChatMessageRepository contactChatMessageRepository;

    @Transactional
    public ContactChatMessage register(Long contactChatRoomId, SendContactChatMessageRequest request){

        ContactChatRoom contactChatRoom = contactChatRoomRepository.findById(contactChatRoomId)
                .orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND.getCode(), CHAT_ROOM_NOT_FOUND.getMessage()));

        return contactChatMessageRepository.save(request.toEntity(contactChatRoom));
    }

    @Transactional(readOnly = true)
    public List<ContactChatMessage> getByRoom(Long contactChatRoomId){

        return contactChatMessageRepository.findAllByContactChatRoom_ContactChatRoomId(contactChatRoomId);
    }
}
