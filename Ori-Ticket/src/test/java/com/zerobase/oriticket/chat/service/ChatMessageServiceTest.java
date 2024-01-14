package com.zerobase.oriticket.chat.service;

import com.zerobase.oriticket.domain.chat.dto.SendChatMessageRequest;
import com.zerobase.oriticket.domain.chat.entity.ChatMessage;
import com.zerobase.oriticket.domain.chat.entity.ChatRoom;
import com.zerobase.oriticket.domain.chat.repository.ChatMessageRepository;
import com.zerobase.oriticket.domain.chat.repository.ChatRoomRepository;
import com.zerobase.oriticket.domain.chat.service.ChatMessageService;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.members.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ChatMessageServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChatMessageService chatMessageService;

    private final static Long MEMBER_ID = 5L;
    private final static Long CHAT_ROOM_ID = 10L;
    private final static Long CHAT_MESSAGE_ID = 4L;
    private final static String MESSAGE = "message";

    private ChatRoom createChatRoom(Long chatRoomId){
        return ChatRoom.builder()
                .chatRoomId(chatRoomId)
                .build();
    }

    private ChatMessage createChatMessage(Long chatMessageId, ChatRoom chatRoom, Member member, String message){
        return ChatMessage.builder()
                .chatMessageId(chatMessageId)
                .chatRoom(chatRoom)
                .member(member)
                .message(message)
                .chattedAt(LocalDateTime.now())
                .build();
    }

    private Member createMember(Long membersId){
        return Member.builder()
                .memberId(membersId)
                .build();
    }

    @Test
    @Transactional
    @DisplayName("채팅 메세지 등록 성공")
    void successRegister(){
        //given
        SendChatMessageRequest sendRequest =
                SendChatMessageRequest.builder()
                        .memberId(MEMBER_ID)
                        .message(MESSAGE)
                        .build();
        Member member = createMember(MEMBER_ID);
        ChatRoom chatRoom = createChatRoom(CHAT_ROOM_ID);
        ChatMessage chatMessage = createChatMessage(CHAT_MESSAGE_ID, chatRoom, member, MESSAGE);

        given(chatRoomRepository.findById(anyLong()))
                .willReturn(Optional.of(chatRoom));
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(member));
        given(chatMessageRepository.save(any(ChatMessage.class)))
                .willReturn(chatMessage);

        //when
        ChatMessage fetchedChatMessage = chatMessageService.register(CHAT_ROOM_ID, sendRequest);

        //then
        assertThat(fetchedChatMessage.getChatMessageId()).isEqualTo(4L);
        assertThat(fetchedChatMessage.getChatRoom()).isEqualTo(chatRoom);
        assertThat(fetchedChatMessage.getMember().getMemberId()).isEqualTo(5L);
        assertThat(fetchedChatMessage.getMessage()).isEqualTo("message");
        assertNotNull(fetchedChatMessage.getChattedAt());
    }

    @Test
    @DisplayName("채팅 방에 있는 메시지 조회 성공")
    void successGetByRoom(){
        //given

        Member member1 = createMember(40L);
        Member member2 = createMember(50L);
        ChatRoom chatRoom = createChatRoom(1L);
        ChatMessage chatMessage1 = createChatMessage(1L, chatRoom, member1, "message1");
        ChatMessage chatMessage2 = createChatMessage(2L, chatRoom, member2, "message2");

        List<ChatMessage> chatMessageList = Arrays.asList(chatMessage1, chatMessage2);

        given(chatMessageRepository.findAllByChatRoom_ChatRoomId(anyLong()))
                .willReturn(chatMessageList);

        //when
        List<ChatMessage> fetchedChatMessages = chatMessageService.getByRoom(1L);

        //then
        assertThat(fetchedChatMessages).hasSize(2);
        assertThat(fetchedChatMessages.get(0).getChatMessageId()).isEqualTo(1L);
        assertThat(fetchedChatMessages.get(0).getChatRoom()).isEqualTo(chatRoom);
        assertThat(fetchedChatMessages.get(0).getMember().getMemberId()).isEqualTo(40L);
        assertThat(fetchedChatMessages.get(0).getMessage()).isEqualTo("message1");
        assertNotNull(fetchedChatMessages.get(0).getChattedAt());
        assertThat(fetchedChatMessages.get(1).getChatMessageId()).isEqualTo(2L);
        assertThat(fetchedChatMessages.get(1).getChatRoom()).isEqualTo(chatRoom);
        assertThat(fetchedChatMessages.get(1).getMember().getMemberId()).isEqualTo(50L);
        assertThat(fetchedChatMessages.get(1).getMessage()).isEqualTo("message2");
        assertNotNull(fetchedChatMessages.get(1).getChattedAt());
    }
}
