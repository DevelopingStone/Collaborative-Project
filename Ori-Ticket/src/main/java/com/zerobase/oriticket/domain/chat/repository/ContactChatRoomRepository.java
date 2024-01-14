package com.zerobase.oriticket.domain.chat.repository;

import com.zerobase.oriticket.domain.chat.entity.ContactChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactChatRoomRepository extends JpaRepository<ContactChatRoom, Long> {
    Optional<ContactChatRoom> findByMember_MemberId(Long memberId);
}
