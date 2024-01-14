package com.zerobase.oriticket.chat.controller;

import com.zerobase.oriticket.domain.chat.controller.ChatRoomController;
import com.zerobase.oriticket.domain.chat.entity.ChatRoom;
import com.zerobase.oriticket.domain.chat.service.ChatRoomService;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.post.entity.Post;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatRoomController.class)
public class ChatRoomControllerTest {

    @MockBean
    private ChatRoomService chatRoomService;

    @Autowired
    private MockMvc mockMvc;

    private final static String BASE_URL = "/chatroom";

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
                .salePost(createSalePost(1L))
                .build();
    }

    private Post createSalePost(Long salePostId){
        return Post.builder()
                .salePostId(salePostId)
                .build();
    }

    @Test
    @DisplayName("ChatRoom 조회 성공")
    void successGet() throws Exception {
        //given
        Transaction transaction = createTransaction(1L);
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        List<Member> members = List.of(member1, member2);
        ChatRoom chatRoom = createChatRoom(1L, transaction, members);

        given(chatRoomService.get(anyLong()))
                .willReturn(chatRoom);

        //when
        //then
        mockMvc.perform(get(BASE_URL+"?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatRoomId").value(1L))
                .andExpect(jsonPath("$.salePostId").value(1L))
                .andExpect(jsonPath("$.transactionId").value(1L))
                .andExpect(jsonPath("$.members[0].memberId").value(1L))
                .andExpect(jsonPath("$.members[1].memberId").value(2L))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @DisplayName("모든 ChatRoom 조회 성공")
    void successGetAll() throws Exception {
        //given
        Transaction transaction1 = createTransaction(1L);
        Transaction transaction2 = createTransaction(2L);
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        Member member3 = createMember(3L);
        Member member4 = createMember(4L);
        List<Member> members1 = List.of(member1, member2);
        List<Member> members2 = List.of(member3, member4);
        ChatRoom chatRoom1 = createChatRoom(1L, transaction1, members1);
        ChatRoom chatRoom2 = createChatRoom(2L, transaction2, members2);

        List<ChatRoom> chatRoomList = Arrays.asList(chatRoom1, chatRoom2);
        Page<ChatRoom> chatRooms = new PageImpl<>(chatRoomList);

        given(chatRoomService.getAll(anyInt(), anyInt()))
                .willReturn(chatRooms);

        //when
        //then
        mockMvc.perform(get(BASE_URL+"/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].chatRoomId").value(1L))
                .andExpect(jsonPath("$.content[0].transactionId").value(1L))
                .andExpect(jsonPath("$.content[0].members[0].memberId").value(1L))
                .andExpect(jsonPath("$.content[0].members[1].memberId").value(2L))
                .andExpect(jsonPath("$.content[0].createdAt").exists())
                .andExpect(jsonPath("$.content[1].chatRoomId").value(2L))
                .andExpect(jsonPath("$.content[1].transactionId").value(2L))
                .andExpect(jsonPath("$.content[1].members[0].memberId").value(3L))
                .andExpect(jsonPath("$.content[1].members[1].memberId").value(4L))
                .andExpect(jsonPath("$.content[1].createdAt").exists());
    }

    @Test
    @DisplayName("Transaction 으로 ChatRoom 조회 성공")
    void successGetByTransaction() throws Exception {
        //given
        Transaction transaction = createTransaction(1L);
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        List<Member> members = List.of(member1, member2);
        ChatRoom chatRoom = createChatRoom(1L, transaction, members);

        given(chatRoomService.getByTransaction(anyLong()))
                .willReturn(chatRoom);

        //when
        //then
        mockMvc.perform(get(BASE_URL+"/transactions?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatRoomId").value(1L))
                .andExpect(jsonPath("$.transactionId").value(1L))
                .andExpect(jsonPath("$.members[0].memberId").value(1L))
                .andExpect(jsonPath("$.members[1].memberId").value(2L))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @DisplayName("Member 로 ChatRoom 조회 성공")
    void successGetByMember() throws Exception {
        //given
        Transaction transaction1 = createTransaction(1L);
        Transaction transaction2 = createTransaction(2L);
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        Member member3 = createMember(3L);
        List<Member> members1 = List.of(member1, member2);
        List<Member> members2 = List.of(member1, member3);
        ChatRoom chatRoom1 = createChatRoom(1L, transaction1, members1);
        ChatRoom chatRoom2 = createChatRoom(2L, transaction2, members2);

        List<ChatRoom> chatRooms = Arrays.asList(chatRoom1, chatRoom2);

        given(chatRoomService.getByMember(anyLong()))
                .willReturn(chatRooms);

        //when
        //then
        mockMvc.perform(get(BASE_URL+"/member?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].chatRoomId").value(1L))
                .andExpect(jsonPath("$.[0].transactionId").value(1L))
                .andExpect(jsonPath("$.[0].members[0].memberId").value(1L))
                .andExpect(jsonPath("$.[0].members[1].memberId").value(2L))
                .andExpect(jsonPath("$.[0].createdAt").exists())
                .andExpect(jsonPath("$.[1].chatRoomId").value(2L))
                .andExpect(jsonPath("$.[1].transactionId").value(2L))
                .andExpect(jsonPath("$.[1].members[0].memberId").value(1L))
                .andExpect(jsonPath("$.[1].members[1].memberId").value(3L))
                .andExpect(jsonPath("$.[1].createdAt").exists());
    }
}
