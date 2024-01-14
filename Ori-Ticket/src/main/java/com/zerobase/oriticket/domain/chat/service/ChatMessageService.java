package com.zerobase.oriticket.domain.chat.service;

import com.zerobase.oriticket.domain.chat.dto.SendChatMessageRequest;
import com.zerobase.oriticket.domain.chat.entity.ChatMessage;
import com.zerobase.oriticket.domain.chat.entity.ChatRoom;
import com.zerobase.oriticket.domain.chat.repository.ChatMessageRepository;
import com.zerobase.oriticket.domain.chat.repository.ChatRoomRepository;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.members.repository.UserRepository;
import com.zerobase.oriticket.global.exception.impl.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zerobase.oriticket.global.constants.ChatExceptionStatus.ALREADY_ENDED_CHAT_ROOM;
import static com.zerobase.oriticket.global.constants.ChatExceptionStatus.CHAT_ROOM_NOT_FOUND;
import static com.zerobase.oriticket.global.constants.MemberExceptionStatus.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatMessage register(Long chatRoomId, SendChatMessageRequest request){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND.getCode(), CHAT_ROOM_NOT_FOUND.getMessage()));

        Member member = userRepository.findById(request.getMemberId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND.getCode(), MEMBER_NOT_FOUND.getMessage()));

        validateCanSendMessage(chatRoom);

        ChatMessage message = request.toEntity(chatRoom, member);

        return chatMessageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getByRoom(Long chatRoomId) {

        return chatMessageRepository.findAllByChatRoom_ChatRoomId(chatRoomId);
    }

    private void validateCanSendMessage(ChatRoom chatRoom){
        if(chatRoom.getEndedAt() != null){
            throw new CustomException(ALREADY_ENDED_CHAT_ROOM.getCode(),
                    ALREADY_ENDED_CHAT_ROOM.getMessage());
        }
    }
}
