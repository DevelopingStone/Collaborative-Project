package com.zerobase.oriticket.chat.service;

import com.zerobase.oriticket.domain.chat.constants.ContactSenderType;
import com.zerobase.oriticket.domain.chat.dto.SendContactChatMessageRequest;
import com.zerobase.oriticket.domain.chat.entity.ContactChatMessage;
import com.zerobase.oriticket.domain.chat.entity.ContactChatRoom;
import com.zerobase.oriticket.domain.chat.repository.ContactChatMessageRepository;
import com.zerobase.oriticket.domain.chat.repository.ContactChatRoomRepository;
import com.zerobase.oriticket.domain.chat.service.ContactChatMessageService;
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
public class ContactChatMessageServiceTest {

    @Mock
    private ContactChatRoomRepository contactChatRoomRepository;

    @Mock
    private ContactChatMessageRepository contactChatMessageRepository;

    @InjectMocks
    private ContactChatMessageService contactChatMessageService;

    @Test
    @Transactional
    @DisplayName("문의 채팅 메세지 등록 성공")
    void successRegister(){
        //given
        SendContactChatMessageRequest sendRequest =
                SendContactChatMessageRequest.builder()
                        .senderType("admin")
                        .message("message")
                        .build();

        ContactChatRoom contactChatRoom = ContactChatRoom.builder()
                .contactChatRoomId(11L)
                .build();

        given(contactChatRoomRepository.findById(anyLong()))
                .willReturn(Optional.of(contactChatRoom));
        given(contactChatMessageRepository.save(any(ContactChatMessage.class)))
                .willReturn(ContactChatMessage.builder()
                        .contactChatMessageId(1L)
                        .contactChatRoom(contactChatRoom)
                        .senderType(ContactSenderType.ADMIN)
                        .message("message")
                        .chattedAt(LocalDateTime.now())
                        .build());

        //when
        ContactChatMessage fetchedChatMessage = contactChatMessageService.register(11L, sendRequest);

        //then
        assertThat(fetchedChatMessage.getContactChatMessageId()).isEqualTo(1L);
        assertThat(fetchedChatMessage.getContactChatRoom()).isEqualTo(contactChatRoom);
        assertThat(fetchedChatMessage.getSenderType()).isEqualTo(ContactSenderType.ADMIN);
        assertThat(fetchedChatMessage.getMessage()).isEqualTo("message");
        assertNotNull(fetchedChatMessage.getChattedAt());
    }

    @Test
    @DisplayName("문의 채팅방에 있는 메세지 조회 성공")
    void successGetByRoom(){
        //given
        ContactChatRoom contactChatRoom = ContactChatRoom.builder()
                .contactChatRoomId(11L)
                .build();

        ContactChatMessage contactChatMessage1 =
                ContactChatMessage.builder()
                        .contactChatMessageId(1L)
                        .contactChatRoom(contactChatRoom)
                        .senderType(ContactSenderType.ADMIN)
                        .message("message")
                        .chattedAt(LocalDateTime.now())
                        .build();

        ContactChatMessage contactChatMessage2 =
                ContactChatMessage.builder()
                        .contactChatMessageId(2L)
                        .contactChatRoom(contactChatRoom)
                        .senderType(ContactSenderType.MEMBER)
                        .message("message")
                        .chattedAt(LocalDateTime.now())
                        .build();

        List<ContactChatMessage> contactChatMessageList = Arrays.asList(contactChatMessage1, contactChatMessage2);

        given(contactChatMessageRepository.findAllByContactChatRoom_ContactChatRoomId(anyLong()))
                .willReturn(contactChatMessageList);


        //when
        List<ContactChatMessage> fetchedContactChatMessages = contactChatMessageService.getByRoom(11L);

        //then
        assertThat(fetchedContactChatMessages).hasSize(2);
        assertThat(fetchedContactChatMessages.get(0).getContactChatMessageId()).isEqualTo(1L);
        assertThat(fetchedContactChatMessages.get(0).getContactChatRoom()).isEqualTo(contactChatRoom);
        assertThat(fetchedContactChatMessages.get(0).getSenderType()).isEqualTo(ContactSenderType.ADMIN);
        assertThat(fetchedContactChatMessages.get(0).getMessage()).isEqualTo("message");
        assertNotNull(fetchedContactChatMessages.get(0).getChattedAt());
        assertThat(fetchedContactChatMessages.get(1).getContactChatMessageId()).isEqualTo(2L);
        assertThat(fetchedContactChatMessages.get(1).getContactChatRoom()).isEqualTo(contactChatRoom);
        assertThat(fetchedContactChatMessages.get(1).getSenderType()).isEqualTo(ContactSenderType.MEMBER);
        assertThat(fetchedContactChatMessages.get(1).getMessage()).isEqualTo("message");
        assertNotNull(fetchedContactChatMessages.get(1).getChattedAt());

    }
}
