package com.zerobase.oriticket.domain.chat.repository;

import com.zerobase.oriticket.domain.chat.entity.ContactChatMessage;
import com.zerobase.oriticket.domain.chat.entity.ContactChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactChatMessageRepository extends JpaRepository<ContactChatMessage, Long> {
    List<ContactChatMessage> findAllByContactChatRoom_ContactChatRoomId(Long contactChatRoomId);
}
