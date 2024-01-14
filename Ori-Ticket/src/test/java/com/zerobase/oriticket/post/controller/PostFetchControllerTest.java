package com.zerobase.oriticket.post.controller;

import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import com.zerobase.oriticket.domain.post.controller.PostFetchController;
import com.zerobase.oriticket.domain.post.entity.*;
import com.zerobase.oriticket.domain.post.service.PostFetchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostFetchController.class)
public class PostFetchControllerTest {

    @MockBean
    private PostFetchService postFetchService;

    @Autowired
    private MockMvc mockMvc;

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

    private Post createPost(Long salePostId, Member member, SaleStatus saleStatus, Ticket ticket){
        return Post.builder()
                .salePostId(salePostId)
                .member(member)
                .ticket(ticket)
                .saleStatus(saleStatus)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Member createMember(Long membersId){
        return Member.builder()
                .memberId(membersId)
                .build();
    }

    @Test
    @DisplayName("특정 멤버의 판매중인 SalePost 조회 성공")
    void successGetByMemberIdAndSaleStatus() throws Exception {
        //given
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "한화");
        Ticket ticket1 = createTicket(1L, sports, stadium, awayTeam);
        Ticket ticket2 = createTicket(2L, sports, stadium, awayTeam);
        Member member = createMember(1L);
        Post post1 = createPost(1L, member, SaleStatus.FOR_SALE, ticket1);
        Post post2 = createPost(2L, member, SaleStatus.TRADING, ticket2);
        List<Post> postList = Arrays.asList(post1, post2);

        given(postFetchService.get(anyLong(), anyList()))
                .willReturn(postList);

        //when
        //then
        mockMvc.perform(get("/members/1/posts?status=for_sale, trading"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].salePostId").value(1L))
                .andExpect(jsonPath("$.[0].memberId").value(1L))
                .andExpect(jsonPath("$.[0].ticket.sportsName").value("야구"))
                .andExpect(jsonPath("$.[0].ticket.stadiumName").value("고척돔"))
                .andExpect(jsonPath("$.[0].ticket.homeTeamName").value("키움"))
                .andExpect(jsonPath("$.[0].ticket.awayTeamName").value("한화"))
                .andExpect(jsonPath("$.[0].ticket.quantity").value(1))
                .andExpect(jsonPath("$.[0].ticket.salePrice").value(10000))
                .andExpect(jsonPath("$.[0].ticket.originalPrice").value(15000))
                .andExpect(jsonPath("$.[0].ticket.expirationAt").exists())
                .andExpect(jsonPath("$.[0].ticket.isSuccessive").value(false))
                .andExpect(jsonPath("$.[0].ticket.seatInfo").value("A열 4석"))
                .andExpect(jsonPath("$.[0].ticket.imgUrl").value("image url"))
                .andExpect(jsonPath("$.[0].ticket.note").value("note"))
                .andExpect(jsonPath("$.[0].saleStatus").value("FOR_SALE"))
                .andExpect(jsonPath("$.[0].createdAt").exists())
                .andExpect(jsonPath("$.[1].salePostId").value(2L))
                .andExpect(jsonPath("$.[1].memberId").value(1L))
                .andExpect(jsonPath("$.[1].ticket.sportsName").value("야구"))
                .andExpect(jsonPath("$.[1].ticket.stadiumName").value("고척돔"))
                .andExpect(jsonPath("$.[1].ticket.homeTeamName").value("키움"))
                .andExpect(jsonPath("$.[1].ticket.awayTeamName").value("한화"))
                .andExpect(jsonPath("$.[1].ticket.quantity").value(1))
                .andExpect(jsonPath("$.[1].ticket.salePrice").value(10000))
                .andExpect(jsonPath("$.[1].ticket.originalPrice").value(15000))
                .andExpect(jsonPath("$.[1].ticket.expirationAt").exists())
                .andExpect(jsonPath("$.[1].ticket.isSuccessive").value(false))
                .andExpect(jsonPath("$.[1].ticket.seatInfo").value("A열 4석"))
                .andExpect(jsonPath("$.[1].ticket.imgUrl").value("image url"))
                .andExpect(jsonPath("$.[1].ticket.note").value("note"))
                .andExpect(jsonPath("$.[1].saleStatus").value("TRADING"))
                .andExpect(jsonPath("$.[1].createdAt").exists());
    }

    @Test
    @DisplayName("특정 멤버의 판매종료인 SalePost 조회 성공")
    void successGetByMemberIdAndEndStatus() throws Exception {
        //given
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "한화");
        Ticket ticket1 = createTicket(1L, sports, stadium, awayTeam);
        Ticket ticket2 = createTicket(2L, sports, stadium, awayTeam);
        Member member = createMember(1L);
        Post post1 = createPost(1L, member, SaleStatus.SOLD, ticket1);
        Post post2 = createPost(2L, member, SaleStatus.REPORTED, ticket2);
        List<Post> postList = Arrays.asList(post1, post2);

        given(postFetchService.get(anyLong(), anyList()))
                .willReturn(postList);

        //when
        //then
        mockMvc.perform(get("/members/1/posts?status=sold, reported"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].salePostId").value(1L))
                .andExpect(jsonPath("$.[0].memberId").value(1L))
                .andExpect(jsonPath("$.[0].ticket.sportsName").value("야구"))
                .andExpect(jsonPath("$.[0].ticket.stadiumName").value("고척돔"))
                .andExpect(jsonPath("$.[0].ticket.homeTeamName").value("키움"))
                .andExpect(jsonPath("$.[0].ticket.awayTeamName").value("한화"))
                .andExpect(jsonPath("$.[0].ticket.quantity").value(1))
                .andExpect(jsonPath("$.[0].ticket.salePrice").value(10000))
                .andExpect(jsonPath("$.[0].ticket.originalPrice").value(15000))
                .andExpect(jsonPath("$.[0].ticket.expirationAt").exists())
                .andExpect(jsonPath("$.[0].ticket.isSuccessive").value(false))
                .andExpect(jsonPath("$.[0].ticket.seatInfo").value("A열 4석"))
                .andExpect(jsonPath("$.[0].ticket.imgUrl").value("image url"))
                .andExpect(jsonPath("$.[0].ticket.note").value("note"))
                .andExpect(jsonPath("$.[0].saleStatus").value("SOLD"))
                .andExpect(jsonPath("$.[0].createdAt").exists())
                .andExpect(jsonPath("$.[1].salePostId").value(2L))
                .andExpect(jsonPath("$.[1].memberId").value(1L))
                .andExpect(jsonPath("$.[1].ticket.sportsName").value("야구"))
                .andExpect(jsonPath("$.[1].ticket.stadiumName").value("고척돔"))
                .andExpect(jsonPath("$.[1].ticket.homeTeamName").value("키움"))
                .andExpect(jsonPath("$.[1].ticket.awayTeamName").value("한화"))
                .andExpect(jsonPath("$.[1].ticket.quantity").value(1))
                .andExpect(jsonPath("$.[1].ticket.salePrice").value(10000))
                .andExpect(jsonPath("$.[1].ticket.originalPrice").value(15000))
                .andExpect(jsonPath("$.[1].ticket.expirationAt").exists())
                .andExpect(jsonPath("$.[1].ticket.isSuccessive").value(false))
                .andExpect(jsonPath("$.[1].ticket.seatInfo").value("A열 4석"))
                .andExpect(jsonPath("$.[1].ticket.imgUrl").value("image url"))
                .andExpect(jsonPath("$.[1].ticket.note").value("note"))
                .andExpect(jsonPath("$.[1].saleStatus").value("REPORTED"))
                .andExpect(jsonPath("$.[1].createdAt").exists());
    }

}
