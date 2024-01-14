package com.zerobase.oriticket.report.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.oriticket.domain.members.entity.Member;
import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import com.zerobase.oriticket.domain.post.entity.*;
import com.zerobase.oriticket.domain.report.constants.ReportReactStatus;
import com.zerobase.oriticket.domain.report.constants.ReportTransactionType;
import com.zerobase.oriticket.domain.report.controller.ReportTransactionController;
import com.zerobase.oriticket.domain.report.dto.RegisterReportTransactionRequest;
import com.zerobase.oriticket.domain.report.dto.UpdateReportRequest;
import com.zerobase.oriticket.domain.report.entity.ReportTransaction;
import com.zerobase.oriticket.domain.report.service.ReportTransactionService;
import com.zerobase.oriticket.domain.transaction.constants.TransactionStatus;
import com.zerobase.oriticket.domain.transaction.entity.Transaction;
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

@WebMvcTest(ReportTransactionController.class)
public class ReportTransactionControllerTest {

    @MockBean
    private ReportTransactionService reportTransactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final static String BASE_URL = "/transactions";

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

    private Transaction createTransaction(
            Long transactionId,
            Post salePost,
            Member member
    ){
        return Transaction.builder()
                .transactionId(transactionId)
                .salePost(salePost)
                .member(member)
                .startedAt(LocalDateTime.now())
                .status(TransactionStatus.PENDING)
                .build();
    }

    private ReportTransaction createReportTransaction(
            Long reportTransactionId,
            Member member,
            Transaction transaction,
            ReportReactStatus status,
            LocalDateTime reactedAt,
            String note
    ){
        return ReportTransaction.builder()
                .reportTransactionId(reportTransactionId)
                .member(member)
                .transaction(transaction)
                .reason(ReportTransactionType.ECONOMIC_LOSS)
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
    @DisplayName("Report Transaction 등록 성공")
    void successRegister() throws Exception {
        //given
        RegisterReportTransactionRequest registerRequest =
                RegisterReportTransactionRequest.builder()
                        .memberId(2L)
                        .reason("Economic_loss")
                        .build();
        Sports sports = createSports(1L, "야구");
        Stadium stadium = createStadium(1L, sports, "고척돔", "키움");
        AwayTeam awayTeam = createAwayTeam(1L, sports, "두산");
        Ticket ticket = createTicket(10L, sports, stadium, awayTeam);
        Member member1 = createMember(11L);
        Member member2 = createMember(2L);
        Post salePost = createPost(14L, member1, ticket, SaleStatus.FOR_SALE);
        Transaction transaction = createTransaction(19L, salePost, member2);
        ReportTransaction reportTransaction = createReportTransaction(5L, member2, transaction,
                        ReportReactStatus.PROCESSING, null, null);

        given(reportTransactionService.register(anyLong(), any(RegisterReportTransactionRequest.class)))
                .willReturn(reportTransaction);

        //when
        //then
        mockMvc.perform(post(BASE_URL+"/19/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportTransactionId").value(5L))
                .andExpect(jsonPath("$.memberId").value(2L))
                .andExpect(jsonPath("$.transaction.transactionId").value(19L))
                .andExpect(jsonPath("$.transaction.salePostId").value(14L))
                .andExpect(jsonPath("$.transaction.sellerId").value(11L))
                .andExpect(jsonPath("$.transaction.buyerId").value(2L))
                .andExpect(jsonPath("$.transaction.payAmount").doesNotExist())
                .andExpect(jsonPath("$.transaction.status").value("Pending"))
                .andExpect(jsonPath("$.transaction.receivedAt").doesNotExist())
                .andExpect(jsonPath("$.transaction.startedAt").exists())
                .andExpect(jsonPath("$.transaction.endedAt").doesNotExist())
                .andExpect(jsonPath("$.reason").value(ReportTransactionType.ECONOMIC_LOSS.getReportType()))
                .andExpect(jsonPath("$.reportedAt").exists())
                .andExpect(jsonPath("$.status").value("PROCESSING"))
                .andExpect(jsonPath("$.reactedAt").doesNotExist())
                .andExpect(jsonPath("$.note").doesNotExist());
    }

    @Test
    @DisplayName("Report Transaction 처리 성공")
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
        Transaction transaction = createTransaction(19L, salePost, member2);
        ReportTransaction reportTransaction = createReportTransaction(5L, member2, transaction,
                        ReportReactStatus.REACTED, LocalDateTime.now(), "react note");

        given(reportTransactionService.updateToReacted(anyLong(), any(UpdateReportRequest.class)))
                .willReturn(reportTransaction);

        //when
        //then
        mockMvc.perform(patch(BASE_URL+"/report/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportTransactionId").value(5L))
                .andExpect(jsonPath("$.memberId").value(2L))
                .andExpect(jsonPath("$.transaction.transactionId").value(19L))
                .andExpect(jsonPath("$.transaction.salePostId").value(14L))
                .andExpect(jsonPath("$.transaction.sellerId").value(11L))
                .andExpect(jsonPath("$.transaction.buyerId").value(2L))
                .andExpect(jsonPath("$.transaction.payAmount").doesNotExist())
                .andExpect(jsonPath("$.transaction.status").value("Pending"))
                .andExpect(jsonPath("$.transaction.receivedAt").doesNotExist())
                .andExpect(jsonPath("$.transaction.startedAt").exists())
                .andExpect(jsonPath("$.transaction.endedAt").doesNotExist())
                .andExpect(jsonPath("$.reason").value(ReportTransactionType.ECONOMIC_LOSS.getReportType()))
                .andExpect(jsonPath("$.reportedAt").exists())
                .andExpect(jsonPath("$.status").value("REACTED"))
                .andExpect(jsonPath("$.reactedAt").exists())
                .andExpect(jsonPath("$.note").value("react note"));
    }
}
