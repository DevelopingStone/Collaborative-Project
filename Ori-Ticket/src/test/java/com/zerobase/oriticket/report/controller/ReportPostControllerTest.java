package com.zerobase.oriticket.report.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import com.zerobase.oriticket.domain.post.entity.*;
import com.zerobase.oriticket.domain.report.constants.ReportPostType;
import com.zerobase.oriticket.domain.report.constants.ReportReactStatus;
import com.zerobase.oriticket.domain.report.controller.ReportPostController;
import com.zerobase.oriticket.domain.report.dto.RegisterReportPostRequest;
import com.zerobase.oriticket.domain.report.dto.UpdateReportRequest;
import com.zerobase.oriticket.domain.report.entity.ReportPost;
import com.zerobase.oriticket.domain.report.service.ReportPostService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportPostController.class)
public class ReportPostControllerTest {

    @MockBean
    private ReportPostService reportPostService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final static String BASE_URL = "/posts";

    private final static Integer QUANTITY = 1;
    private final static Integer SALE_PRICE = 10000;
    private final static Integer ORIGINAL_PRICE = 20000;
    private final static boolean IS_SUCCESSIVE = false;
    private final static String SEAT_INFO = "seat info";
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

    private Post createPost(Long salePostId, Member member, Ticket ticket, SaleStatus status){
        return Post.builder()
                .salePostId(salePostId)
                .member(member)
                .ticket(ticket)
                .saleStatus(status)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private ReportPost createReportPost(
            Long reportPostId,
            Member member,
            Post salePost,
            ReportReactStatus status,
            LocalDateTime reactedAt,
            String note
    ){
        return ReportPost.builder()
                .reportPostId(reportPostId)
                .member(member)
                .salePost(salePost)
                .reason(ReportPostType.OTHER_ISSUES)
                .reportedAt(LocalDateTime.now())
                .status(status)
                .reactedAt(reactedAt)
                .note(note)
                .build();
    }

    private Member createMember(Long membersId){
        return Member.builder()
                .memberId(membersId)
                .build();
    }

    @Test
    @DisplayName("Report Post 등록 성공")
    void successRegister() throws Exception {
        //given
        RegisterReportPostRequest registerRequest =
                RegisterReportPostRequest.builder()
                        .memberId(2L)
                        .reason("Other_issues")
                        .build();
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "두산");
        Ticket ticket = createTicket(10L, sports, stadium, awayTeam);
        Member member1 = createMember(11L);
        Member member2 = createMember(2L);
        Post salePost = createPost(14L, member1, ticket, SaleStatus.FOR_SALE);
        ReportPost reportPost = createReportPost(5L, member2, salePost,
                ReportReactStatus.PROCESSING, null, null);

        given(reportPostService.register(anyLong(), any(RegisterReportPostRequest.class)))
                .willReturn(reportPost);

        //when
        //then
        mockMvc.perform(post(BASE_URL+"/14/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportPostId").value(5L))
                .andExpect(jsonPath("$.memberId").value(2L))
                .andExpect(jsonPath("$.salePost.salePostId").value(14L))
                .andExpect(jsonPath("$.salePost.memberId").value(11L))
                .andExpect(jsonPath("$.salePost.ticket.sportsName").value("야구"))
                .andExpect(jsonPath("$.salePost.ticket.stadiumName").value("고척돔"))
                .andExpect(jsonPath("$.salePost.ticket.homeTeamName").value("키움"))
                .andExpect(jsonPath("$.salePost.ticket.awayTeamName").value("두산"))
                .andExpect(jsonPath("$.salePost.ticket.quantity").value(1))
                .andExpect(jsonPath("$.salePost.ticket.salePrice").value(10000))
                .andExpect(jsonPath("$.salePost.ticket.originalPrice").value(20000))
                .andExpect(jsonPath("$.salePost.ticket.expirationAt").exists())
                .andExpect(jsonPath("$.salePost.ticket.isSuccessive").value(false))
                .andExpect(jsonPath("$.salePost.ticket.seatInfo").value("seat info"))
                .andExpect(jsonPath("$.salePost.ticket.imgUrl").value("image url"))
                .andExpect(jsonPath("$.salePost.ticket.note").value("note"))
                .andExpect(jsonPath("$.salePost.saleStatus").value("FOR_SALE"))
                .andExpect(jsonPath("$.salePost.createdAt").exists())
                .andExpect(jsonPath("$.reason").value(ReportPostType.OTHER_ISSUES.getReportType()))
                .andExpect(jsonPath("$.reportedAt").exists())
                .andExpect(jsonPath("$.status").value("PROCESSING"))
                .andExpect(jsonPath("$.reactedAt").doesNotExist())
                .andExpect(jsonPath("$.note").doesNotExist());
    }

    @Test
    @DisplayName("Report Post 처리 성공")
    void successUpdateToReacted() throws Exception {
        //given
        UpdateReportRequest updateRequest =
                UpdateReportRequest.builder()
                        .note("react note")
                        .build();
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "두산");
        Ticket ticket = createTicket(10L, sports, stadium, awayTeam);
        Member member1 = createMember(11L);
        Member member2 = createMember(2L);
        Post salePost = createPost(14L, member1, ticket, SaleStatus.FOR_SALE);
        ReportPost reportPost = createReportPost(5L, member2, salePost,
                ReportReactStatus.REACTED, LocalDateTime.now(), "react note");

        given(reportPostService.updateToReacted(anyLong(), any(UpdateReportRequest.class)))
                .willReturn(reportPost);

        //when
        //then
        mockMvc.perform(patch(BASE_URL+"/report/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportPostId").value(5L))
                .andExpect(jsonPath("$.memberId").value(2L))
                .andExpect(jsonPath("$.salePost.salePostId").value(14L))
                .andExpect(jsonPath("$.salePost.memberId").value(11L))
                .andExpect(jsonPath("$.salePost.ticket.sportsName").value("야구"))
                .andExpect(jsonPath("$.salePost.ticket.stadiumName").value("고척돔"))
                .andExpect(jsonPath("$.salePost.ticket.homeTeamName").value("키움"))
                .andExpect(jsonPath("$.salePost.ticket.awayTeamName").value("두산"))
                .andExpect(jsonPath("$.salePost.ticket.quantity").value(1))
                .andExpect(jsonPath("$.salePost.ticket.salePrice").value(10000))
                .andExpect(jsonPath("$.salePost.ticket.originalPrice").value(20000))
                .andExpect(jsonPath("$.salePost.ticket.expirationAt").exists())
                .andExpect(jsonPath("$.salePost.ticket.isSuccessive").value(false))
                .andExpect(jsonPath("$.salePost.ticket.seatInfo").value("seat info"))
                .andExpect(jsonPath("$.salePost.ticket.imgUrl").value("image url"))
                .andExpect(jsonPath("$.salePost.ticket.note").value("note"))
                .andExpect(jsonPath("$.salePost.saleStatus").value("FOR_SALE"))
                .andExpect(jsonPath("$.salePost.createdAt").exists())
                .andExpect(jsonPath("$.reason").value(ReportPostType.OTHER_ISSUES.getReportType()))
                .andExpect(jsonPath("$.reportedAt").exists())
                .andExpect(jsonPath("$.status").value("REACTED"))
                .andExpect(jsonPath("$.reactedAt").exists())
                .andExpect(jsonPath("$.note").value("react note"));
    }
}
