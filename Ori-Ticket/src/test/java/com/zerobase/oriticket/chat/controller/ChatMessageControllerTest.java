package com.zerobase.oriticket.chat.controller;

import com.zerobase.oriticket.domain.chat.constants.ContactSenderType;
import com.zerobase.oriticket.domain.chat.controller.ChatMessageController;
import com.zerobase.oriticket.domain.chat.entity.ChatMessage;
import com.zerobase.oriticket.domain.chat.entity.ChatRoom;
import com.zerobase.oriticket.domain.chat.entity.ContactChatMessage;
import com.zerobase.oriticket.domain.chat.entity.ContactChatRoom;
import com.zerobase.oriticket.domain.chat.service.ChatMessageService;
import com.zerobase.oriticket.domain.chat.service.ContactChatMessageService;
import com.zerobase.oriticket.domain.members.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatMessageController.class)
public class ChatMessageControllerTest {

    @MockBean
    private ChatMessageService chatMessageService;

    @MockBean
    private ContactChatMessageService contactChatMessageService;

    @Autowired
    private MockMvc mockMvc;

    private final static String BASE_URL = "/message";

    private final static String MESSAGE = "message";

    private ChatRoom createChatRoom(Long chatRoomId){
        return ChatRoom.builder()
                .chatRoomId(chatRoomId)
                .createdAt(LocalDateTime.now())
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
    @DisplayName("ChatRoom 에 있는 모든 ChatMessage 조회 성공")
    void successGetAllChatMessage() throws Exception {
        //given
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        ChatRoom chatRoom = createChatRoom(1L);
        ChatMessage chatMessage1 = createChatMessage(1L, chatRoom, member1, MESSAGE);
        ChatMessage chatMessage2 = createChatMessage(2L, chatRoom, member2, MESSAGE);

        List<ChatMessage> chatMessages = Arrays.asList(chatMessage1, chatMessage2);

        given(chatMessageService.getByRoom(anyLong()))
                .willReturn(chatMessages);

        //when
        //then
        mockMvc.perform(get(BASE_URL+"?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].chatMessageId").value(1L))
                .andExpect(jsonPath("$.[0].chatRoomId").value(1L))
                .andExpect(jsonPath("$.[0].memberId").value(1L))
                .andExpect(jsonPath("$.[0].message").value("message"))
                .andExpect(jsonPath("$.[0].chattedAt").exists())
                .andExpect(jsonPath("$.[1].chatMessageId").value(2L))
                .andExpect(jsonPath("$.[1].chatRoomId").value(1L))
                .andExpect(jsonPath("$.[1].memberId").value(2L))
                .andExpect(jsonPath("$.[1].message").value("message"))
                .andExpect(jsonPath("$.[1].chattedAt").exists());
    }

    @Test
    @DisplayName("ContactChatRoom 에 있는 모든 ContactChatMessage 조회 성공")
    void successGetAllContactMessage() throws Exception {
        //given
        ContactChatRoom contactChatRoom = ContactChatRoom.builder()
                .contactChatRoomId(1L)
                .createdAt(LocalDateTime.now())
                .build();

        ContactChatMessage contactChatMessage1 =
                ContactChatMessage.builder()
                        .contactChatMessageId(1L)
                        .contactChatRoom(contactChatRoom)
                        .senderType(ContactSenderType.MEMBER)
                        .message(MESSAGE)
                        .chattedAt(LocalDateTime.now())
                        .build();

        ContactChatMessage contactChatMessage2 =
                ContactChatMessage.builder()
                        .contactChatMessageId(2L)
                        .contactChatRoom(contactChatRoom)
                        .senderType(ContactSenderType.ADMIN)
                        .message(MESSAGE)
                        .chattedAt(LocalDateTime.now())
                        .build();

        List<ContactChatMessage> contactChatMessages =
                Arrays.asList(contactChatMessage1, contactChatMessage2);

        given(contactChatMessageService.getByRoom(anyLong()))
                .willReturn(contactChatMessages);

        //when
        //then
        mockMvc.perform(get(BASE_URL+"/contact?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].contactChatMessageId").value(1L))
                .andExpect(jsonPath("$.[0].contactChatRoomId").value(1L))
                .andExpect(jsonPath("$.[0].senderType").value("MEMBER"))
                .andExpect(jsonPath("$.[0].message").value("message"))
                .andExpect(jsonPath("$.[0].chattedAt").exists())
                .andExpect(jsonPath("$.[1].contactChatMessageId").value(2L))
                .andExpect(jsonPath("$.[1].contactChatRoomId").value(1L))
                .andExpect(jsonPath("$.[1].senderType").value("ADMIN"))
                .andExpect(jsonPath("$.[1].message").value("message"))
                .andExpect(jsonPath("$.[1].chattedAt").exists());
    }
}
