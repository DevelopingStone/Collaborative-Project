package com.zerobase.oriticket.domain.chat.repository;

import com.zerobase.oriticket.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByTransaction_TransactionId(Long transactionId);
    List<ChatRoom> findAllByMembers_MemberId(Long memberId);
    boolean existsByTransaction_TransactionId(Long transactionId);
}
