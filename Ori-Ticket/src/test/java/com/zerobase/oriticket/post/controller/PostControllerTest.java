package com.zerobase.oriticket.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import com.zerobase.oriticket.domain.post.controller.PostController;
import com.zerobase.oriticket.domain.post.dto.RegisterPostRequest;
import com.zerobase.oriticket.domain.post.dto.UpdateStatusToReportedPostRequest;
import com.zerobase.oriticket.domain.post.entity.*;
import com.zerobase.oriticket.domain.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @MockBean
    private PostService postService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final static String BASE_URL = "/posts";

    private final static Long SALE_POST_ID = 1L;
    private final static Long MEMBER_ID = 1L;
    private final static Long SPORTS_ID = 1L;
    private final static Long STADIUM_ID = 1L;
    private final static Long AWAY_TEAM_ID = 1L;
    private final static Long TICKET_ID = 1L;
    private final static Integer QUANTITY = 1;
    private final static Integer SALE_PRICE = 10000;
    private final static Integer ORIGINAL_PRICE = 20000;
    private final static LocalDateTime EXPIRATION_AT = LocalDateTime.now().plusDays(1);
    private final static boolean IS_SUCCESSIVE = false;
    private final static String SEAT_INFO = "seat info";
    private final static String IMG_URL = "image url";
    private final static String NOTE = "note";

    private final static String SPORTS_NAME = "야구";
    private final static String STADIUM_NAME = "고척돔";
    private final static String HOME_TEAM_NAME = "키움";
    private final static String AWAY_TEAM_NAME = "한화";

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

    private Post createPost(Long salePostId, Member member, Ticket ticket, SaleStatus status){
        return Post.builder()
                .salePostId(salePostId)
                .member(member)
                .ticket(ticket)
                .saleStatus(status)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Member createMember(Long membersId){
        return Member.builder()
                .memberId(membersId)
                .build();
    }

    @Test
    @DisplayName("SalePost 등록 성공")
    void successRegister() throws Exception {
        //given
        RegisterPostRequest postRequest =
                RegisterPostRequest.builder()
                        .memberId(MEMBER_ID)
                        .sportsId(SPORTS_ID)
                        .stadiumId(STADIUM_ID)
                        .awayTeamId(AWAY_TEAM_ID)
                        .quantity(QUANTITY)
                        .salePrice(SALE_PRICE)
                        .originalPrice(ORIGINAL_PRICE)
                        .expirationAt(EXPIRATION_AT)
                        .isSuccessive(IS_SUCCESSIVE)
                        .seatInfo(SEAT_INFO)
                        .imgUrl(IMG_URL)
                        .note(NOTE)
                        .build();
        Sports sports = createSports(SPORTS_ID, SPORTS_NAME);
        Stadium stadium = createStadium(STADIUM_ID, sports, STADIUM_NAME, HOME_TEAM_NAME);
        AwayTeam awayTeam = createAwayTeam(AWAY_TEAM_ID, sports, AWAY_TEAM_NAME);
        Ticket ticket = createTicket(TICKET_ID, sports, stadium, awayTeam);
        Member member = createMember(MEMBER_ID);
        Post salePost = createPost(SALE_POST_ID, member, ticket, SaleStatus.FOR_SALE);

        given(postService.registerPost(any(RegisterPostRequest.class)))
                .willReturn(salePost);
        //when
        //then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salePostId").value(1L))
                .andExpect(jsonPath("$.memberId").value(1L))
                .andExpect(jsonPath("$.ticket.sportsName").value("야구"))
                .andExpect(jsonPath("$.ticket.stadiumName").value("고척돔"))
                .andExpect(jsonPath("$.ticket.homeTeamName").value("키움"))
                .andExpect(jsonPath("$.ticket.awayTeamName").value("한화"))
                .andExpect(jsonPath("$.ticket.quantity").value(1))
                .andExpect(jsonPath("$.ticket.salePrice").value(10000))
                .andExpect(jsonPath("$.ticket.originalPrice").value(20000))
                .andExpect(jsonPath("$.ticket.isSuccessive").value(false))
                .andExpect(jsonPath("$.ticket.seatInfo").value("seat info"))
                .andExpect(jsonPath("$.ticket.imgUrl").value("image url"))
                .andExpect(jsonPath("$.ticket.note").value("note"))
                .andExpect(jsonPath("$.saleStatus").value("FOR_SALE"));
    }

    @Test
    @DisplayName("SalePost 조회 성공")
    void successGet() throws Exception {
        //given
        Sports sports = createSports(SPORTS_ID, SPORTS_NAME);
        Stadium stadium = createStadium(STADIUM_ID, sports, STADIUM_NAME, HOME_TEAM_NAME);
        AwayTeam awayTeam = createAwayTeam(AWAY_TEAM_ID, sports, AWAY_TEAM_NAME);
        Ticket ticket = createTicket(TICKET_ID, sports, stadium, awayTeam);
        Member member = createMember(MEMBER_ID);
        Post salePost = createPost(SALE_POST_ID, member, ticket, SaleStatus.FOR_SALE);

        given(postService.get(anyLong()))
                .willReturn(salePost);

        //when
        //then
        mockMvc.perform(get(BASE_URL+"?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salePostId").value(1L))
                .andExpect(jsonPath("$.memberId").value(1L))
                .andExpect(jsonPath("$.ticket.sportsName").value("야구"))
                .andExpect(jsonPath("$.ticket.stadiumName").value("고척돔"))
                .andExpect(jsonPath("$.ticket.homeTeamName").value("키움"))
                .andExpect(jsonPath("$.ticket.awayTeamName").value("한화"))
                .andExpect(jsonPath("$.ticket.quantity").value(1))
                .andExpect(jsonPath("$.ticket.salePrice").value(10000))
                .andExpect(jsonPath("$.ticket.originalPrice").value(20000))
                .andExpect(jsonPath("$.ticket.isSuccessive").value(false))
                .andExpect(jsonPath("$.ticket.seatInfo").value("seat info"))
                .andExpect(jsonPath("$.ticket.imgUrl").value("image url"))
                .andExpect(jsonPath("$.ticket.note").value("note"))
                .andExpect(jsonPath("$.saleStatus").value("FOR_SALE"));
    }

    @Test
    @DisplayName("SalePost 삭제 성공")
    void successDelete() throws Exception {
        //given
        given(postService.delete(anyLong()))
                .willReturn(SALE_POST_ID);

        //when
        //then
        mockMvc.perform(delete(BASE_URL+"?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1L));
    }

    @Test
    @DisplayName("SalePost 의 상태를 Reported 로 업데이트 성공")
    void successUpdateToReported() throws Exception {
        //given
        UpdateStatusToReportedPostRequest postRequest =
                UpdateStatusToReportedPostRequest.builder()
                        .salePostId(SALE_POST_ID)
                        .build();
        Sports sports = createSports(SPORTS_ID, SPORTS_NAME);
        Stadium stadium = createStadium(STADIUM_ID, sports, STADIUM_NAME, HOME_TEAM_NAME);
        AwayTeam awayTeam = createAwayTeam(AWAY_TEAM_ID, sports, AWAY_TEAM_NAME);
        Ticket ticket = createTicket(TICKET_ID, sports, stadium, awayTeam);
        Member member = createMember(MEMBER_ID);
        Post salePost = createPost(SALE_POST_ID, member, ticket, SaleStatus.REPORTED);

        given(postService.updateToReported(any(UpdateStatusToReportedPostRequest.class)))
                .willReturn(salePost);

        //when
        //then
        mockMvc.perform(patch(BASE_URL+"/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salePostId").value(1L))
                .andExpect(jsonPath("$.memberId").value(1L))
                .andExpect(jsonPath("$.ticket.sportsName").value("야구"))
                .andExpect(jsonPath("$.ticket.stadiumName").value("고척돔"))
                .andExpect(jsonPath("$.ticket.homeTeamName").value("키움"))
                .andExpect(jsonPath("$.ticket.awayTeamName").value("한화"))
                .andExpect(jsonPath("$.ticket.quantity").value(1))
                .andExpect(jsonPath("$.ticket.salePrice").value(10000))
                .andExpect(jsonPath("$.ticket.originalPrice").value(20000))
                .andExpect(jsonPath("$.ticket.isSuccessive").value(false))
                .andExpect(jsonPath("$.ticket.seatInfo").value("seat info"))
                .andExpect(jsonPath("$.ticket.imgUrl").value("image url"))
                .andExpect(jsonPath("$.ticket.note").value("note"))
                .andExpect(jsonPath("$.saleStatus").value("REPORTED"));
    }

}
