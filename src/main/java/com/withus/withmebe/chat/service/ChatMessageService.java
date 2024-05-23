package com.withus.withmebe.chat.service;

import static com.withus.withmebe.chat.type.MessageType.CHAT;
import static com.withus.withmebe.chat.type.MessageType.JOIN;
import static com.withus.withmebe.chat.type.MessageType.LEAVE;
import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;

import com.withus.withmebe.chat.dto.ChatMessageDto;
import com.withus.withmebe.chat.dto.request.ChatMessageRequestDto;
import com.withus.withmebe.chat.entity.ChatMessage;
import com.withus.withmebe.chat.entity.ChatRoom;
import com.withus.withmebe.chat.repository.ChatMessageRepository;
import com.withus.withmebe.chat.repository.ChatRoomRepository;
import com.withus.withmebe.chat.type.MessageType;
import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ChatMessageService {

  private final ChatMessageRepository chatMessageRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final MemberRepository memberRepository;

  public ChatMessageDto join(Long memberId, Long roomId) {
    return createChatMessage(roomId, memberId, JOIN, JOIN.getValue()).toChatMessageDto();
  }

  public ChatMessageDto leave(Long memberId, Long roomId) {
    return createChatMessage(roomId, memberId, LEAVE, LEAVE.getValue()).toChatMessageDto();
  }

  @Transactional
  public ChatMessageDto chat(Long memberId, ChatMessageRequestDto request) {
    ChatMessage chatMessage = createChatMessage(
        request.chatroomId(), memberId, CHAT, request.content());
    readChatRoomByIdOrThrow(request.chatroomId()).updateByMessage(chatMessage);
    return chatMessage.toChatMessageDto();
  }

  private ChatMessage createChatMessage(
      Long roomId, Long memberId, MessageType type, String content) {
    return chatMessageRepository.save(ChatMessage.builder()
        .chatRoom(readChatRoomByIdOrThrow(roomId))
        .content(content)
        .type(type)
        .chatMember(readMemberByIdOrThrow(memberId))
        .build());
  }

  private ChatRoom readChatRoomByIdOrThrow(Long roomId) {
    return chatRoomRepository.findById(roomId)
        .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
  }

  private Member readMemberByIdOrThrow(Long memberId) {
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
  }

}

