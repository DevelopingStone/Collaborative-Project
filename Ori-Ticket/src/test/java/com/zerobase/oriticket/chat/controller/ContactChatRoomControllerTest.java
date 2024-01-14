package com.zerobase.oriticket.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.oriticket.domain.chat.controller.ContactChatRoomController;
import com.zerobase.oriticket.domain.chat.dto.RegisterContactChatRoomRequest;
import com.zerobase.oriticket.domain.chat.entity.ContactChatRoom;
import com.zerobase.oriticket.domain.chat.service.ContactChatRoomService;
import com.zerobase.oriticket.domain.members.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactChatRoomController.class)
public class ContactChatRoomControllerTest {

    @MockBean
    private ContactChatRoomService contactChatRoomService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final static String BASE_URL = "/contact";

    private ContactChatRoom createContactChatRoom(Long contactChatRoomId, Member member){
        return ContactChatRoom.builder()
                .contactChatRoomId(contactChatRoomId)
                .member(member)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Member createMember(Long membersId){
        return Member.builder()
                .memberId(membersId)
                .build();
    }

    @Test
    @DisplayName("ContactChatRoom 등록 성공")
    void successRegister() throws Exception{
        //given
        RegisterContactChatRoomRequest contactChatRoomRequest =
                RegisterContactChatRoomRequest.builder()
                        .memberId(1L)
                        .build();
        Member member = createMember(1L);
        ContactChatRoom contactChatRoom = createContactChatRoom(1L, member);

        given(contactChatRoomService.register(any(RegisterContactChatRoomRequest.class)))
                .willReturn(contactChatRoom);

        //when
        //then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contactChatRoomRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contactChatRoomId").value(1L))
                .andExpect(jsonPath("$.memberId").value(1L))
                .andExpect(jsonPath("$.createdAt").exists());

    }

    @Test
    @DisplayName("ContactChatRoom 조회 성공")
    void successGet() throws Exception{
        //given
        Member member = createMember(1L);
        ContactChatRoom contactChatRoom = createContactChatRoom(1L, member);

        given(contactChatRoomService.get(anyLong()))
                .willReturn(contactChatRoom);

        //when
        //then
        mockMvc.perform(get(BASE_URL+"?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contactChatRoomId").value(1L))
                .andExpect(jsonPath("$.memberId").value(1L))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @DisplayName("모든 ContactChatRoom 조회 성공")
    void successGetAll() throws Exception{
        //given
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        ContactChatRoom contactChatRoom1 = createContactChatRoom(1L, member1);
        ContactChatRoom contactChatRoom2 = createContactChatRoom(2L, member2);

        List<ContactChatRoom> contactChatRoomList = Arrays.asList(contactChatRoom1, contactChatRoom2);
        Page<ContactChatRoom> contactChatRooms = new PageImpl<>(contactChatRoomList);

        given(contactChatRoomService.getAll(anyInt(), anyInt()))
                .willReturn(contactChatRooms);

        //when
        //then
        mockMvc.perform(get(BASE_URL+"/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].contactChatRoomId").value(1L))
                .andExpect(jsonPath("$.content[0].memberId").value(1L))
                .andExpect(jsonPath("$.content[0]createdAt").exists())
                .andExpect(jsonPath("$.content[1].contactChatRoomId").value(2L))
                .andExpect(jsonPath("$.content[1].memberId").value(2L))
                .andExpect(jsonPath("$.content[1]createdAt").exists());
    }

    @Test
    @DisplayName("Member 로 ContactChatRoom 조회 성공")
    void successGetByMember() throws Exception {
        //given
        Member member = createMember(1L);
        ContactChatRoom contactChatRoom = createContactChatRoom(1L, member);

        given(contactChatRoomService.getByMember(anyLong()))
                .willReturn(contactChatRoom);

        //when
        //then
        mockMvc.perform(get(BASE_URL + "/member?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contactChatRoomId").value(1L))
                .andExpect(jsonPath("$.memberId").value(1L))
                .andExpect(jsonPath("$.createdAt").exists());
    }

}
