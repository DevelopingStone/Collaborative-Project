package com.zerobase.oriticket.chat.service;

import com.zerobase.oriticket.domain.chat.entity.ChatRoom;
import com.zerobase.oriticket.domain.chat.repository.ChatRoomRepository;
import com.zerobase.oriticket.domain.chat.service.ChatRoomService;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ChatRoomServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @InjectMocks
    private ChatRoomService chatRoomService;

    private ChatRoom createChatRoom(Long chatRoomId, Transaction transaction, List<Member> members){
        return ChatRoom.builder()
                .chatRoomId(chatRoomId)
                .transaction(transaction)
                .members(members)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Member createMember(Long membersId){
        return Member.builder()
                .memberId(membersId)
                .build();
    }

    private Transaction createTransaction(Long transactionId){
        return Transaction.builder()
                .transactionId(transactionId)
                .build();
    }

    @Test
    @DisplayName("채팅 방 조회 성공")
    void successGet(){
        //given
        Transaction transaction = createTransaction(10L);
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        List<Member> members = List.of(member1, member2);
        ChatRoom chatRoom = createChatRoom(5L, transaction, members);

        given(chatRoomRepository.findById(anyLong()))
                .willReturn(Optional.of(chatRoom));

        //when
        ChatRoom fetchedChatRoom = chatRoomService.get(5L);

        //then
        assertThat(fetchedChatRoom.getChatRoomId()).isEqualTo(5L);
        assertThat(fetchedChatRoom.getTransaction()).isEqualTo(transaction);
        assertThat(fetchedChatRoom.getMembers()).hasSize(2);
        assertThat(fetchedChatRoom.getMembers()).isEqualTo(members);
        assertNotNull(fetchedChatRoom.getCreatedAt());
    }

    @Test
    @DisplayName("모든 채팅 방 조회 성공")
    void successGetAll(){
        //given
        Transaction transaction1 = createTransaction(10L);
        Transaction transaction2 = createTransaction(11L);
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        List<Member> members = List.of(member1, member2);
        ChatRoom chatRoom1 = createChatRoom(5L, transaction1, members);
        ChatRoom chatRoom2 = createChatRoom(6L, transaction2, members);

        List<ChatRoom> chatRoomList = Arrays.asList(chatRoom1, chatRoom2);

        given(chatRoomRepository.findAll(any(Pageable.class)))
                .willReturn(new PageImpl<>(chatRoomList));

        //when
        Page<ChatRoom> fetchedChatRooms = chatRoomService.getAll(1, 10);

        //then
        assertThat(fetchedChatRooms.getContent()).hasSize(2);
        assertThat(fetchedChatRooms.getContent().get(0).getChatRoomId()).isEqualTo(5L);
        assertThat(fetchedChatRooms.getContent().get(0).getTransaction()).isEqualTo(transaction1);
        assertThat(fetchedChatRooms.getContent().get(0).getMembers()).isEqualTo(members);
        assertNotNull(fetchedChatRooms.getContent().get(0).getCreatedAt());
        assertThat(fetchedChatRooms.getContent().get(1).getChatRoomId()).isEqualTo(6L);
        assertThat(fetchedChatRooms.getContent().get(1).getTransaction()).isEqualTo(transaction2);
        assertThat(fetchedChatRooms.getContent().get(1).getMembers()).isEqualTo(members);
        assertNotNull(fetchedChatRooms.getContent().get(1).getCreatedAt());

    }

    @Test
    @DisplayName("Transaction 으로 채팅 방 조회 성공")
    void successGetByTransaction(){
        //given
        Transaction transaction = createTransaction(10L);
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        List<Member> members = List.of(member1, member2);
        ChatRoom chatRoom = createChatRoom(5L, transaction, members);

        given(chatRoomRepository.findByTransaction_TransactionId(anyLong()))
                .willReturn(Optional.of(chatRoom));

        //when
        ChatRoom fetchedChatRoom = chatRoomService.getByTransaction(10L);

        //then
        assertThat(fetchedChatRoom.getChatRoomId()).isEqualTo(5L);
        assertThat(fetchedChatRoom.getTransaction()).isEqualTo(transaction);
        assertThat(fetchedChatRoom.getMembers()).isEqualTo(members);
        assertNotNull(fetchedChatRoom.getCreatedAt());

    }

    @Test
    @DisplayName("Member 로 채팅 방 조회 성공")
    void successGetByMember(){
        //given
        Transaction transaction1 = createTransaction(10L);
        Transaction transaction2 = createTransaction(11L);
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        List<Member> members = List.of(member1, member2);
        ChatRoom chatRoom1 = createChatRoom(5L, transaction1, members);
        ChatRoom chatRoom2 = createChatRoom(10L, transaction2, members);

        List<ChatRoom> chatRoomList = Arrays.asList(chatRoom1, chatRoom2);

        given(chatRoomRepository.findAllByMembers_MemberId(anyLong()))
                .willReturn(chatRoomList);

        //when
        List<ChatRoom> fetchedChatRooms = chatRoomService.getByMember(1L);

        //then
        assertThat(fetchedChatRooms).hasSize(2);
        assertThat(fetchedChatRooms.get(0).getChatRoomId()).isEqualTo(5L);
        assertThat(fetchedChatRooms.get(0).getTransaction()).isEqualTo(transaction1);
        assertThat(fetchedChatRooms.get(0).getMembers()).isEqualTo(members);
        assertNotNull(fetchedChatRooms.get(0).getCreatedAt());
        assertThat(fetchedChatRooms.get(1).getChatRoomId()).isEqualTo(10L);
        assertThat(fetchedChatRooms.get(1).getTransaction()).isEqualTo(transaction2);
        assertThat(fetchedChatRooms.get(1).getMembers()).isEqualTo(members);
        assertNotNull(fetchedChatRooms.get(1).getCreatedAt());

    }
}
