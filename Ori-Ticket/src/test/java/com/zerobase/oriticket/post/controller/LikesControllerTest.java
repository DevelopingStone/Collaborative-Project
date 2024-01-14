package com.zerobase.oriticket.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.post.controller.LikesController;
import com.zerobase.oriticket.domain.post.dto.RegisterLikesRequest;
import com.zerobase.oriticket.domain.post.entity.*;
import com.zerobase.oriticket.domain.post.service.LikesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LikesController.class)
public class LikesControllerTest {

    @MockBean
    private LikesService likesService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final static Integer QUANTITY = 1;
    private final static Integer SALE_PRICE = 10000;
    private final static Integer ORIGINAL_PRICE = 15000;
    private final static Boolean IS_SUCCESSIVE = false;
    private final static String SEAT_INFO = "A열 4석";
    private final static String IMG_URL = "image url";
    private final static String NOTE = "note";

    private Sports createSports(Long sportsId, String sportsName){
        return Sports.builder()
                .sportsId(sportsId)
                .sportsName(sportsName)
                .build();
    }

    private Stadium createStadium(Long stadiumId, Sports sports, String stadiumName, String homeTeamName){
        return Stadium.builder()
                .stadiumId(stadiumId)
                .sports(sports)
                .stadiumName(stadiumName)
                .homeTeamName(homeTeamName)
                .build();
    }

    private AwayTeam createAwayTeam(Long awayTeamId, Sports sports, String awayTeamName){
        return AwayTeam.builder()
                .awayTeamId(awayTeamId)
                .sports(sports)
                .awayTeamName(awayTeamName)
                .build();
    }

    private Ticket createTicket(Long ticketId, Sports sports, Stadium stadium, AwayTeam awayTeam){
        return Ticket.builder()
                .ticketId(ticketId)
                .sports(sports)
                .stadium(stadium)
                .awayTeam(awayTeam)
                .quantity(QUANTITY)
                .salePrice(SALE_PRICE)
                .originalPrice(ORIGINAL_PRICE)
                .expirationAt(LocalDateTime.now().plusDays(5))
                .isSuccessive(IS_SUCCESSIVE)
                .seatInfo(SEAT_INFO)
                .imgUrl(IMG_URL)
                .note(NOTE)
                .build();
    }

    private Post createPost(Long salePostId, Member member, Ticket ticket){
        return Post.builder()
                .salePostId(salePostId)
                .member(member)
                .ticket(ticket)
                .build();
    }

    private Likes createLikes(Long likesId, Member member, Post salePost){
        return Likes.builder()
                .likesId(likesId)
                .member(member)
                .salePost(salePost)
                .build();
    }

    private Member createMember(Long membersId){
        return Member.builder()
                .memberId(membersId)
                .build();
    }

        @Test
    @DisplayName("찜하기에 등록 완료")
    void successRegister() throws Exception {
        //given
        RegisterLikesRequest registerRequest = RegisterLikesRequest.builder()
                .memberId(1L)
                .build();
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "한화");
        Ticket ticket = createTicket(12L, sports, stadium, awayTeam);
        Member member = createMember(10L);
        Member seller = createMember(11L);
        Post salePost = createPost(50L, seller, ticket);
        Likes likes = createLikes(1L, member, salePost);

        given(likesService.register(anyLong(), any(RegisterLikesRequest.class)))
                .willReturn(likes);

        //when
        //then
        mockMvc.perform(post("/posts/1/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likesId").value(1L))
                .andExpect(jsonPath("$.memberId").value(10L))
                .andExpect(jsonPath("$.salePost.salePostId").value(50L))
                .andExpect(jsonPath("$.salePost.ticket.sportsName").value("야구"))
                .andExpect(jsonPath("$.salePost.ticket.stadiumName").value("고척돔"))
                .andExpect(jsonPath("$.salePost.ticket.homeTeamName").value("키움"))
                .andExpect(jsonPath("$.salePost.ticket.awayTeamName").value("한화"))
                .andExpect(jsonPath("$.salePost.ticket.quantity").value(1))
                .andExpect(jsonPath("$.salePost.ticket.salePrice").value(10000))
                .andExpect(jsonPath("$.salePost.ticket.originalPrice").value(15000))
                .andExpect(jsonPath("$.salePost.ticket.expirationAt").exists())
                .andExpect(jsonPath("$.salePost.ticket.isSuccessive").value(false))
                .andExpect(jsonPath("$.salePost.ticket.seatInfo").value("A열 4석"))
                .andExpect(jsonPath("$.salePost.ticket.imgUrl").value("image url"))
                .andExpect(jsonPath("$.salePost.ticket.note").value("note"));

    }

    @Test
    @DisplayName("찜하기에서 삭제 완료")
    void successDelete() throws Exception {
        //given
        given(likesService.delete(anyLong(), anyLong()))
                .willReturn(1L);

        //when
        //then
        mockMvc.perform(delete("/posts/1/likes?memberId=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));

    }

    @Test
    @DisplayName("특정 멤버의 모든 찜하기 조회 완료")
    void successGet() throws Exception {
        //given
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "한화");
        Ticket ticket1 = createTicket(12L, sports, stadium, awayTeam);
        Ticket ticket2 = createTicket(13L, sports, stadium, awayTeam);
        Member seller1 = createMember(11L);
        Member seller2 = createMember(12L);
        Post salePost1 = createPost(50L, seller1, ticket1);
        Post salePost2 = createPost(51L, seller2, ticket2);
        Member member = createMember(10L);
        Likes likes1 = createLikes(1L, member, salePost1);
        Likes likes2 = createLikes(2L, member, salePost2);

        List<Likes> likesList = Arrays.asList(likes1, likes2);

        given(likesService.get(anyLong()))
                .willReturn(likesList);

        //when
        //then
        mockMvc.perform(get("/members/10/likes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].likesId").value(1L))
                .andExpect(jsonPath("$.[0].memberId").value(10L))
                .andExpect(jsonPath("$.[0].salePost.salePostId").value(50L))
                .andExpect(jsonPath("$.[0].salePost.ticket.sportsName").value("야구"))
                .andExpect(jsonPath("$.[0].salePost.ticket.stadiumName").value("고척돔"))
                .andExpect(jsonPath("$.[0].salePost.ticket.homeTeamName").value("키움"))
                .andExpect(jsonPath("$.[0].salePost.ticket.awayTeamName").value("한화"))
                .andExpect(jsonPath("$.[0].salePost.ticket.quantity").value(1))
                .andExpect(jsonPath("$.[0].salePost.ticket.salePrice").value(10000))
                .andExpect(jsonPath("$.[0].salePost.ticket.originalPrice").value(15000))
                .andExpect(jsonPath("$.[0].salePost.ticket.expirationAt").exists())
                .andExpect(jsonPath("$.[0].salePost.ticket.isSuccessive").value(false))
                .andExpect(jsonPath("$.[0].salePost.ticket.seatInfo").value("A열 4석"))
                .andExpect(jsonPath("$.[0].salePost.ticket.imgUrl").value("image url"))
                .andExpect(jsonPath("$.[0].salePost.ticket.note").value("note"))
                .andExpect(jsonPath("$.[1].likesId").value(2L))
                .andExpect(jsonPath("$.[1].memberId").value(10L))
                .andExpect(jsonPath("$.[1].salePost.salePostId").value(51L))
                .andExpect(jsonPath("$.[1].salePost.ticket.sportsName").value("야구"))
                .andExpect(jsonPath("$.[1].salePost.ticket.stadiumName").value("고척돔"))
                .andExpect(jsonPath("$.[1].salePost.ticket.homeTeamName").value("키움"))
                .andExpect(jsonPath("$.[1].salePost.ticket.awayTeamName").value("한화"))
                .andExpect(jsonPath("$.[1].salePost.ticket.quantity").value(1))
                .andExpect(jsonPath("$.[1].salePost.ticket.salePrice").value(10000))
                .andExpect(jsonPath("$.[1].salePost.ticket.originalPrice").value(15000))
                .andExpect(jsonPath("$.[1].salePost.ticket.expirationAt").exists())
                .andExpect(jsonPath("$.[1].salePost.ticket.isSuccessive").value(false))
                .andExpect(jsonPath("$.[1].salePost.ticket.seatInfo").value("A열 4석"))
                .andExpect(jsonPath("$.[1].salePost.ticket.imgUrl").value("image url"))
                .andExpect(jsonPath("$.[1].salePost.ticket.note").value("note"));

    }
}
