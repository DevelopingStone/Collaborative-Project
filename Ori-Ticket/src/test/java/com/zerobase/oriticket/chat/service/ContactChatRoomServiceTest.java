package com.zerobase.oriticket.chat.service;

import com.zerobase.oriticket.domain.chat.dto.RegisterContactChatRoomRequest;
import com.zerobase.oriticket.domain.chat.entity.ContactChatRoom;
import com.zerobase.oriticket.domain.chat.repository.ContactChatRoomRepository;
import com.zerobase.oriticket.domain.chat.service.ContactChatRoomService;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.members.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
public class ContactChatRoomServiceTest {

    @Mock
    private ContactChatRoomRepository contactChatRoomRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ContactChatRoomService contactChatRoomService;

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
    @Transactional
    @DisplayName("문의 채팅 방 등록 성공")
    void successRegister(){
        //given
        RegisterContactChatRoomRequest registerRequest =
                RegisterContactChatRoomRequest.builder()
                        .memberId(10L)
                        .build();
        Member member = createMember(10L);
        ContactChatRoom contactChatRoom = createContactChatRoom(1L, member);

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(member));
        given(contactChatRoomRepository.save(any(ContactChatRoom.class)))
                .willReturn(contactChatRoom);

        //when
        ContactChatRoom fetchedContactChatRoom = contactChatRoomService.register(registerRequest);

        //then
        assertThat(fetchedContactChatRoom.getContactChatRoomId()).isEqualTo(1L);
        assertThat(fetchedContactChatRoom.getMember().getMemberId()).isEqualTo(10L);
        assertNotNull(fetchedContactChatRoom.getCreatedAt());

    }

    @Test
    @DisplayName("문의 채팅 방 조회 성공")
    void successGet(){
        //given
        Member member = createMember(10L);
        ContactChatRoom contactChatRoom = createContactChatRoom(1L, member);

        given(contactChatRoomRepository.findById(anyLong()))
                .willReturn(Optional.of(contactChatRoom));

        //when
        ContactChatRoom fetchedContactChatRoom = contactChatRoomService.get(1L);

        //then
        assertThat(fetchedContactChatRoom.getContactChatRoomId()).isEqualTo(1L);
        assertThat(fetchedContactChatRoom.getMember().getMemberId()).isEqualTo(10L);
        assertNotNull(fetchedContactChatRoom.getCreatedAt());

    }

    @Test
    @DisplayName("모든 문의 채팅 방 조회 성공")
    void successGetAll(){
        //given
        Member member1 = createMember(10L);
        Member member2 = createMember(11L);
        ContactChatRoom contactChatRoom1 = createContactChatRoom(1L, member1);
        ContactChatRoom contactChatRoom2 = createContactChatRoom(2L, member2);

        List<ContactChatRoom> contactChatRoomList = Arrays.asList(contactChatRoom1, contactChatRoom2);

        given(contactChatRoomRepository.findAll(any(Pageable.class)))
                .willReturn(new PageImpl<>(contactChatRoomList));

        //when
        Page<ContactChatRoom> fetchedContactChatRooms = contactChatRoomService.getAll(1, 10);

        //then
        assertThat(fetchedContactChatRooms.getContent()).hasSize(2);
        assertThat(fetchedContactChatRooms.getContent().get(0).getContactChatRoomId()).isEqualTo(1L);
        assertThat(fetchedContactChatRooms.getContent().get(0).getMember().getMemberId()).isEqualTo(10L);
        assertNotNull(fetchedContactChatRooms.getContent().get(0).getCreatedAt());
        assertThat(fetchedContactChatRooms.getContent().get(1).getContactChatRoomId()).isEqualTo(2L);
        assertThat(fetchedContactChatRooms.getContent().get(1).getMember().getMemberId()).isEqualTo(11L);
        assertNotNull(fetchedContactChatRooms.getContent().get(1).getCreatedAt());

    }

    @Test
    @DisplayName("Member 로 문의 채팅 방 조회 성공")
    void successGetByMember(){
        //given
        Member member = createMember(10L);
        ContactChatRoom contactChatRoom = createContactChatRoom(1L, member);

        given(contactChatRoomRepository.findByMember_MemberId(anyLong()))
                .willReturn(Optional.of(contactChatRoom));

        //when
        ContactChatRoom fetchedContactChatRoom = contactChatRoomService.getByMember(10L);

        //then
        assertThat(fetchedContactChatRoom.getContactChatRoomId()).isEqualTo(1L);
        assertThat(fetchedContactChatRoom.getMember().getMemberId()).isEqualTo(10L);
        assertNotNull(fetchedContactChatRoom.getCreatedAt());
    }
}
