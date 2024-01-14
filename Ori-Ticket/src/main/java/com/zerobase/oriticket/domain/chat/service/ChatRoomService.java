package com.zerobase.oriticket.domain.chat.service;

import com.zerobase.oriticket.domain.chat.entity.ChatRoom;
import com.zerobase.oriticket.domain.chat.repository.ChatRoomRepository;
import com.zerobase.oriticket.global.exception.impl.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zerobase.oriticket.global.constants.ChatExceptionStatus.CHAT_ROOM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    private static final String CREATED_AT = "createdAt";

    @Transactional(readOnly = true)
    public ChatRoom get(Long chatRoomId){

        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND.getCode(), CHAT_ROOM_NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    public Page<ChatRoom> getAll(int page, int size) {

        Sort sort = Sort.by(CREATED_AT).descending();

        Pageable pageable = PageRequest.of(page-1, size, sort);

        return chatRoomRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public ChatRoom getByTransaction(Long transactionId){

        return chatRoomRepository.findByTransaction_TransactionId(transactionId)
                .orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND.getCode(), CHAT_ROOM_NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> getByMember(Long memberId){

        return chatRoomRepository.findAllByMembers_MemberId(memberId);
    }
}
